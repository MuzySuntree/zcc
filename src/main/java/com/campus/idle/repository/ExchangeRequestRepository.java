package com.campus.idle.repository;

import com.campus.idle.entity.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long>, JpaSpecificationExecutor<ExchangeRequest> {
    Optional<ExchangeRequest> findByIdAndDeleted(Long id, Integer deleted);

    boolean existsByItem_IdAndFromUser_IdAndStatusAndDeleted(Long itemId, Long fromUserId, Integer status, Integer deleted);

    List<ExchangeRequest> findByItem_IdAndStatusAndDeleted(Long itemId, Integer status, Integer deleted);
}
