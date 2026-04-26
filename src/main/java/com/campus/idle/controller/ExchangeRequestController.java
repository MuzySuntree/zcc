package com.campus.idle.controller;

import com.campus.idle.common.PageResult;
import com.campus.idle.common.Result;
import com.campus.idle.dto.exchange.ExchangeQueryDTO;
import com.campus.idle.dto.exchange.ExchangeRequestCreateDTO;
import com.campus.idle.dto.exchange.ExchangeRequestHandleDTO;
import com.campus.idle.service.ExchangeRequestService;
import com.campus.idle.vo.exchange.ExchangeRequestVO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange-requests")
@Validated
public class ExchangeRequestController {

    private final ExchangeRequestService exchangeRequestService;

    public ExchangeRequestController(ExchangeRequestService exchangeRequestService) {
        this.exchangeRequestService = exchangeRequestService;
    }

    @PostMapping
    public Result<ExchangeRequestVO> create(@Valid @RequestBody ExchangeRequestCreateDTO dto) {
        return Result.success(exchangeRequestService.create(dto));
    }

    @GetMapping("/sent")
    public Result<PageResult<ExchangeRequestVO>> sent(@Valid ExchangeQueryDTO dto) {
        return Result.success(exchangeRequestService.sent(dto));
    }

    @GetMapping("/received")
    public Result<PageResult<ExchangeRequestVO>> received(@Valid ExchangeQueryDTO dto) {
        return Result.success(exchangeRequestService.received(dto));
    }

    @PutMapping("/{id}/handle")
    public Result<ExchangeRequestVO> handle(@PathVariable Long id, @Valid @RequestBody ExchangeRequestHandleDTO dto) {
        return Result.success(exchangeRequestService.handle(id, dto));
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        exchangeRequestService.cancel(id);
        return Result.success();
    }

    @PutMapping("/{id}/confirm-complete")
    public Result<Void> confirmComplete(@PathVariable Long id) {
        exchangeRequestService.confirmComplete(id);
        return Result.success();
    }
}