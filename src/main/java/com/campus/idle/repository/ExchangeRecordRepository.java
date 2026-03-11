package com.campus.idle.repository;

import com.campus.idle.entity.ExchangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExchangeRecordRepository extends JpaRepository<ExchangeRecord, Long>, JpaSpecificationExecutor<ExchangeRecord> {
}
