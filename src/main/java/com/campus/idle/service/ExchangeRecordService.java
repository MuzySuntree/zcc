package com.campus.idle.service;

import com.campus.idle.common.PageResult;
import com.campus.idle.dto.exchange.ExchangeQueryDTO;
import com.campus.idle.vo.exchange.ExchangeRecordVO;

public interface ExchangeRecordService {
    PageResult<ExchangeRecordVO> myRecords(ExchangeQueryDTO dto);

    PageResult<ExchangeRecordVO> adminRecords(ExchangeQueryDTO dto);
}