package com.campus.idle.service;

import com.campus.idle.common.PageResult;
import com.campus.idle.dto.exchange.ExchangeQueryDTO;
import com.campus.idle.dto.exchange.ExchangeRequestCreateDTO;
import com.campus.idle.dto.exchange.ExchangeRequestHandleDTO;
import com.campus.idle.vo.exchange.ExchangeRequestVO;

public interface ExchangeRequestService {
    ExchangeRequestVO create(ExchangeRequestCreateDTO dto);

    PageResult<ExchangeRequestVO> sent(ExchangeQueryDTO dto);

    PageResult<ExchangeRequestVO> received(ExchangeQueryDTO dto);

    ExchangeRequestVO handle(Long requestId, ExchangeRequestHandleDTO dto);

    void cancel(Long requestId);
}
