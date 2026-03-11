package com.campus.idle.controller;

import com.campus.idle.common.PageResult;
import com.campus.idle.common.Result;
import com.campus.idle.dto.exchange.ExchangeQueryDTO;
import com.campus.idle.service.ExchangeRecordService;
import com.campus.idle.vo.exchange.ExchangeRecordVO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-records")
@Validated
public class ExchangeRecordController {

    private final ExchangeRecordService exchangeRecordService;

    public ExchangeRecordController(ExchangeRecordService exchangeRecordService) {
        this.exchangeRecordService = exchangeRecordService;
    }

    @GetMapping("/my")
    public Result<PageResult<ExchangeRecordVO>> my(@Valid ExchangeQueryDTO dto) {
        return Result.success(exchangeRecordService.myRecords(dto));
    }
}
