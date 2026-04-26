package com.campus.idle.service.impl;

import com.campus.idle.common.PageResult;
import com.campus.idle.dto.exchange.ExchangeQueryDTO;
import com.campus.idle.entity.ExchangeRecord;
import com.campus.idle.entity.SysUser;
import com.campus.idle.repository.ExchangeRecordRepository;
import com.campus.idle.service.ExchangeRecordService;
import com.campus.idle.util.SecurityUtil;
import com.campus.idle.vo.exchange.ExchangeRecordVO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRecordServiceImpl implements ExchangeRecordService {

    private final ExchangeRecordRepository recordRepository;
    private final SecurityUtil securityUtil;

    public ExchangeRecordServiceImpl(ExchangeRecordRepository recordRepository, SecurityUtil securityUtil) {
        this.recordRepository = recordRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ExchangeRecordVO> myRecords(ExchangeQueryDTO dto) {
        SysUser currentUser = securityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize());

        Specification<ExchangeRecord> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(
                    cb.equal(root.get("ownerUser").get("id"), currentUser.getId()),
                    cb.equal(root.get("requesterUser").get("id"), currentUser.getId())
            ));
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ExchangeRecord> page = recordRepository.findAll(specification, pageable);
        List<ExchangeRecordVO> records = page.getContent().stream().map(this::toVO).toList();

        return PageResult.<ExchangeRecordVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(dto.getPageNum())
                .pageSize(dto.getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ExchangeRecordVO> adminRecords(ExchangeQueryDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize());

        Specification<ExchangeRecord> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ExchangeRecord> page = recordRepository.findAll(specification, pageable);
        List<ExchangeRecordVO> records = page.getContent().stream().map(this::toVO).toList();

        return PageResult.<ExchangeRecordVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(dto.getPageNum())
                .pageSize(dto.getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }

    private ExchangeRecordVO toVO(ExchangeRecord record) {
        return ExchangeRecordVO.builder()
                .id(record.getId())
                .requestId(record.getRequest().getId())
                .itemId(record.getItem().getId())
                .itemTitle(record.getItem().getTitle())
                .ownerUserId(record.getOwnerUser().getId())
                .ownerNickname(record.getOwnerUser().getNickname())
                .requesterUserId(record.getRequesterUser().getId())
                .requesterNickname(record.getRequesterUser().getNickname())
                .exchangeTime(record.getExchangeTime())
                .exchangeLocation(record.getExchangeLocation())
                .note(record.getNote())
                .status(record.getStatus())
                .ownerConfirmed(record.getOwnerConfirmed())
                .requesterConfirmed(record.getRequesterConfirmed())
                .build();
    }
}