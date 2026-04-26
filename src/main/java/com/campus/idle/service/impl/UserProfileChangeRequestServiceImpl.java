package com.campus.idle.service.impl;

import com.campus.idle.common.PageResult;
import com.campus.idle.common.ResultCode;
import com.campus.idle.dto.UserProfileChangeRequestCreateDTO;
import com.campus.idle.entity.SysUser;
import com.campus.idle.entity.UserProfileChangeRequest;
import com.campus.idle.exception.BizException;
import com.campus.idle.repository.SysUserRepository;
import com.campus.idle.repository.UserProfileChangeRequestRepository;
import com.campus.idle.service.UserProfileChangeRequestService;
import com.campus.idle.util.SecurityUtil;
import com.campus.idle.vo.UserProfileChangeRequestVO;
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
public class UserProfileChangeRequestServiceImpl implements UserProfileChangeRequestService {

    private final UserProfileChangeRequestRepository requestRepository;
    private final SysUserRepository userRepository;
    private final SecurityUtil securityUtil;

    public UserProfileChangeRequestServiceImpl(UserProfileChangeRequestRepository requestRepository,
                                               SysUserRepository userRepository,
                                               SecurityUtil securityUtil) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public void submit(UserProfileChangeRequestCreateDTO dto) {
        SysUser user = loadCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest();
        request.setUser(user);

        request.setOldNickname(user.getNickname());
        request.setOldRealName(user.getRealName());
        request.setOldPhone(user.getPhone());
        request.setOldEmail(user.getEmail());

        request.setNewNickname(dto.getNewNickname());
        request.setNewRealName(dto.getNewRealName());
        request.setNewPhone(dto.getNewPhone());
        request.setNewEmail(dto.getNewEmail());
        request.setReason(dto.getReason());
        request.setStatus(1);

        requestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<UserProfileChangeRequestVO> myList(int pageNum, int pageSize) {
        SysUser user = loadCurrentUser();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Specification<UserProfileChangeRequest> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), user.getId()));
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<UserProfileChangeRequest> page = requestRepository.findAll(specification, pageable);
        List<UserProfileChangeRequestVO> records = page.getContent().stream().map(this::toVO).toList();

        return PageResult.<UserProfileChangeRequestVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<UserProfileChangeRequestVO> adminList(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page<UserProfileChangeRequest> page = requestRepository.findAll(
                (root, query, cb) -> {
                    query.orderBy(cb.desc(root.get("id")));
                    return cb.conjunction();
                },
                pageable
        );

        List<UserProfileChangeRequestVO> records = page.getContent().stream().map(this::toVO).toList();

        return PageResult.<UserProfileChangeRequestVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public void approve(Long id) {
        SysUser admin = loadCurrentUser();
        if (!securityUtil.isAdmin(admin)) {
            throw new BizException(ResultCode.FORBIDDEN, "无权操作");
        }

        UserProfileChangeRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "申请不存在"));

        if (request.getStatus() != 1) {
            throw new BizException(ResultCode.CONFLICT, "该申请已处理");
        }

        SysUser user = request.getUser();
        user.setNickname(request.getNewNickname());
        user.setRealName(request.getNewRealName());
        user.setPhone(request.getNewPhone());
        user.setEmail(request.getNewEmail());
        userRepository.save(user);

        request.setStatus(2);
        request.setReviewedBy(admin.getId());
        request.setReviewedAt(LocalDateTime.now());
        requestRepository.save(request);
    }

    @Override
    @Transactional
    public void reject(Long id) {
        SysUser admin = loadCurrentUser();
        if (!securityUtil.isAdmin(admin)) {
            throw new BizException(ResultCode.FORBIDDEN, "无权操作");
        }

        UserProfileChangeRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "申请不存在"));

        if (request.getStatus() != 1) {
            throw new BizException(ResultCode.CONFLICT, "该申请已处理");
        }

        request.setStatus(3);
        request.setReviewedBy(admin.getId());
        request.setReviewedAt(LocalDateTime.now());
        requestRepository.save(request);
    }

    private SysUser loadCurrentUser() {
        Long uid = securityUtil.getCurrentUser().getId();
        return userRepository.findById(uid)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));
    }

    private UserProfileChangeRequestVO toVO(UserProfileChangeRequest request) {
        return UserProfileChangeRequestVO.builder()
                .id(request.getId())
                .userId(request.getUser().getId())
                .username(request.getUser().getUsername())
                .nickname(request.getUser().getNickname())
                .oldNickname(request.getOldNickname())
                .oldRealName(request.getOldRealName())
                .oldPhone(request.getOldPhone())
                .oldEmail(request.getOldEmail())
                .newNickname(request.getNewNickname())
                .newRealName(request.getNewRealName())
                .newPhone(request.getNewPhone())
                .newEmail(request.getNewEmail())
                .reason(request.getReason())
                .status(request.getStatus())
                .reviewedBy(request.getReviewedBy())
                .reviewedAt(request.getReviewedAt())
                .createdAt(request.getCreatedAt())
                .build();
    }
}