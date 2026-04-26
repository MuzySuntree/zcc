package com.campus.idle.repository;

import com.campus.idle.entity.ExchangeMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeMessageRepository extends JpaRepository<ExchangeMessage, Long> {

    List<ExchangeMessage> findByRequestIdOrderByIdAsc(Long requestId);
}