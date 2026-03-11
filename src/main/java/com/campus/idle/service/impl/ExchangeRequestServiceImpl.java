package com.campus.idle.service.impl;

import com.campus.idle.common.PageResult;
import com.campus.idle.common.ResultCode;
import com.campus.idle.dto.exchange.ExchangeQueryDTO;
import com.campus.idle.dto.exchange.ExchangeRequestCreateDTO;
import com.campus.idle.dto.exchange.ExchangeRequestHandleDTO;
import com.campus.idle.entity.*;
import com.campus.idle.enums.ExchangeHandleActionEnum;
import com.campus.idle.enums.ExchangeRecordStatusEnum;
import com.campus.idle.enums.ExchangeRequestStatusEnum;
import com.campus.idle.enums.ItemStatusEnum;
import com.campus.idle.exception.BizException;
import com.campus.idle.repository.ExchangeRecordRepository;
import com.campus.idle.repository.ExchangeRequestRepository;
import com.campus.idle.repository.IdleItemRepository;
import com.campus.idle.repository.SysUserRepository;
import com.campus.idle.service.ExchangeRequestService;
import com.campus.idle.util.SecurityUtil;
import com.campus.idle.vo.exchange.ExchangeRequestVO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRequestServiceImpl implements ExchangeRequestService {

    private final ExchangeRequestRepository requestRepository;
    private final ExchangeRecordRepository recordRepository;
    private final IdleItemRepository itemRepository;
    private final SysUserRepository userRepository;
    private final SecurityUtil securityUtil;

    public ExchangeRequestServiceImpl(ExchangeRequestRepository requestRepository,
                                      ExchangeRecordRepository recordRepository,
                                      IdleItemRepository itemRepository,
                                      SysUserRepository userRepository,
                                      SecurityUtil securityUtil) {
        this.requestRepository = requestRepository;
        this.recordRepository = recordRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public ExchangeRequestVO create(ExchangeRequestCreateDTO dto) {
        SysUser fromUser = loadCurrentUser();
        IdleItem item = itemRepository.findByIdAndDeleted(dto.getItemId(), 0)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "物品不存在"));
        if (item.getStatus() != ItemStatusEnum.ON_SHELF.getCode()) {
            throw new BizException(ResultCode.CONFLICT, "当前物品不可申请交换");
        }
        if (item.getUser().getId().equals(fromUser.getId())) {
            throw new BizException(ResultCode.BAD_REQUEST, "不能对自己的物品发起交换申请");
        }
        boolean duplicated = requestRepository.existsByItem_IdAndFromUser_IdAndStatusAndDeleted(
                item.getId(), fromUser.getId(), ExchangeRequestStatusEnum.PENDING.getCode(), 0);
        if (duplicated) {
            throw new BizException(ResultCode.CONFLICT, "你已发起过待处理申请");
        }

        ExchangeRequest request = new ExchangeRequest();
        request.setItem(item);
        request.setFromUser(fromUser);
        request.setToUser(item.getUser());
        request.setMessage(dto.getMessage());
        request.setOfferedItemDesc(dto.getOfferedItemDesc());
        request.setStatus(ExchangeRequestStatusEnum.PENDING.getCode());
        return toVO(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ExchangeRequestVO> sent(ExchangeQueryDTO dto) {
        SysUser currentUser = securityUtil.getCurrentUser();
        return pageByUser(dto, true, currentUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ExchangeRequestVO> received(ExchangeQueryDTO dto) {
        SysUser currentUser = securityUtil.getCurrentUser();
        return pageByUser(dto, false, currentUser.getId());
    }

    @Override
    @Transactional
    public ExchangeRequestVO handle(Long requestId, ExchangeRequestHandleDTO dto) {
        SysUser currentUser = loadCurrentUser();
        ExchangeRequest request = getRequest(requestId);

        if (!request.getToUser().getId().equals(currentUser.getId()) && !securityUtil.isAdmin(currentUser)) {
            throw new BizException(ResultCode.FORBIDDEN, "仅物品拥有者可处理申请");
        }
        if (request.getStatus() != ExchangeRequestStatusEnum.PENDING.getCode()) {
            throw new BizException(ResultCode.CONFLICT, "该申请不是待处理状态");
        }

        ExchangeHandleActionEnum action;
        try {
            action = ExchangeHandleActionEnum.valueOf(dto.getAction().trim().toUpperCase());
        } catch (Exception e) {
            throw new BizException(ResultCode.BAD_REQUEST, "action 仅支持 AGREE 或 REJECT");
        }

        if (action == ExchangeHandleActionEnum.REJECT) {
            request.setStatus(ExchangeRequestStatusEnum.REJECTED.getCode());
            request.setHandledTime(LocalDateTime.now());
            requestRepository.save(request);
            return toVO(request);
        }

        IdleItem item = request.getItem();
        if (item.getStatus() != ItemStatusEnum.ON_SHELF.getCode()) {
            throw new BizException(ResultCode.CONFLICT, "物品状态已变化，无法同意申请");
        }

        request.setStatus(ExchangeRequestStatusEnum.AGREED.getCode());
        request.setHandledTime(LocalDateTime.now());
        requestRepository.save(request);

        item.setStatus(ItemStatusEnum.EXCHANGED.getCode());
        itemRepository.save(item);

        List<ExchangeRequest> pendingOthers = requestRepository.findByItem_IdAndStatusAndDeleted(
                item.getId(), ExchangeRequestStatusEnum.PENDING.getCode(), 0);
        for (ExchangeRequest other : pendingOthers) {
            if (!other.getId().equals(request.getId())) {
                other.setStatus(ExchangeRequestStatusEnum.REJECTED.getCode());
                other.setHandledTime(LocalDateTime.now());
                requestRepository.save(other);
            }
        }

        ExchangeRecord record = new ExchangeRecord();
        record.setRequest(request);
        record.setItem(item);
        record.setOwnerUser(request.getToUser());
        record.setRequesterUser(request.getFromUser());
        record.setExchangeLocation(dto.getExchangeLocation());
        record.setNote(dto.getNote());
        record.setStatus(ExchangeRecordStatusEnum.VALID.getCode());
        recordRepository.save(record);

        return toVO(request);
    }

    @Override
    @Transactional
    public void cancel(Long requestId) {
        SysUser currentUser = loadCurrentUser();
        ExchangeRequest request = getRequest(requestId);
        if (!request.getFromUser().getId().equals(currentUser.getId())) {
            throw new BizException(ResultCode.FORBIDDEN, "仅申请发起者可取消申请");
        }
        if (request.getStatus() != ExchangeRequestStatusEnum.PENDING.getCode()) {
            throw new BizException(ResultCode.CONFLICT, "仅待处理申请可取消");
        }
        request.setStatus(ExchangeRequestStatusEnum.CANCELED.getCode());
        request.setCancelledTime(LocalDateTime.now());
        requestRepository.save(request);
    }

    private PageResult<ExchangeRequestVO> pageByUser(ExchangeQueryDTO dto, boolean sent, Long userId) {
        Pageable pageable = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize());
        Specification<ExchangeRequest> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (sent) {
                predicates.add(cb.equal(root.get("fromUser").get("id"), userId));
            } else {
                predicates.add(cb.equal(root.get("toUser").get("id"), userId));
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<ExchangeRequest> page = requestRepository.findAll(specification, pageable);
        List<ExchangeRequestVO> records = page.getContent().stream().map(this::toVO).toList();
        return PageResult.<ExchangeRequestVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(dto.getPageNum())
                .pageSize(dto.getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }

    private ExchangeRequest getRequest(Long id) {
        return requestRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "交换申请不存在"));
    }

    private SysUser loadCurrentUser() {
        Long uid = securityUtil.getCurrentUser().getId();
        return userRepository.findById(uid).orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));
    }

    private ExchangeRequestVO toVO(ExchangeRequest request) {
        return ExchangeRequestVO.builder()
                .id(request.getId())
                .itemId(request.getItem().getId())
                .itemTitle(request.getItem().getTitle())
                .fromUserId(request.getFromUser().getId())
                .fromUserNickname(request.getFromUser().getNickname())
                .toUserId(request.getToUser().getId())
                .toUserNickname(request.getToUser().getNickname())
                .message(request.getMessage())
                .offeredItemDesc(request.getOfferedItemDesc())
                .status(request.getStatus())
                .handledTime(request.getHandledTime())
                .cancelledTime(request.getCancelledTime())
                .createdAt(request.getCreatedAt())
                .build();
    }
}
