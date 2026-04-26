package com.campus.idle.repository;

import com.campus.idle.entity.ExchangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ExchangeRecordRepository extends JpaRepository<ExchangeRecord, Long>, JpaSpecificationExecutor<ExchangeRecord> {

    Optional<ExchangeRecord> findByRequest_Id(Long requestId);
}