package com.campus.idle.controller;

import com.campus.idle.common.Result;
import com.campus.idle.entity.ExchangeMessage;
import com.campus.idle.entity.ExchangeRequest;
import com.campus.idle.entity.SysUser;
import com.campus.idle.exception.BizException;
import com.campus.idle.repository.ExchangeMessageRepository;
import com.campus.idle.repository.ExchangeRequestRepository;
import com.campus.idle.util.SecurityUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/exchange-messages")
public class ExchangeMessageController {

    private final ExchangeMessageRepository exchangeMessageRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final SecurityUtil securityUtil;

    public ExchangeMessageController(ExchangeMessageRepository exchangeMessageRepository,
                                     ExchangeRequestRepository exchangeRequestRepository,
                                     SecurityUtil securityUtil) {
        this.exchangeMessageRepository = exchangeMessageRepository;
        this.exchangeRequestRepository = exchangeRequestRepository;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/{requestId}")
    public Result<List<ExchangeMessage>> list(@PathVariable Long requestId) {
        SysUser currentUser = securityUtil.getCurrentUser();
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new BizException(null, "交换申请不存在"));

        boolean allowed = request.getFromUser().getId().equals(currentUser.getId())
                || request.getToUser().getId().equals(currentUser.getId())
                || securityUtil.isAdmin(currentUser);

        if (!allowed) {
            throw new BizException(null, "无权查看该协商记录");
        }

        return Result.success(exchangeMessageRepository.findByRequestIdOrderByIdAsc(requestId));
    }

    @PostMapping
    public Result<Void> send(@RequestBody ExchangeMessage message) {
        SysUser currentUser = securityUtil.getCurrentUser();
        ExchangeRequest request = exchangeRequestRepository.findById(message.getRequestId())
                .orElseThrow(() -> new BizException(null, "交换申请不存在"));

        boolean allowed = request.getFromUser().getId().equals(currentUser.getId())
                || request.getToUser().getId().equals(currentUser.getId())
                || securityUtil.isAdmin(currentUser);

        if (!allowed) {
            throw new BizException(null, "无权发送协商消息");
        }

        message.setFromUserId(currentUser.getId());

        if (request.getFromUser().getId().equals(currentUser.getId())) {
            message.setToUserId(request.getToUser().getId());
        } else {
            message.setToUserId(request.getFromUser().getId());
        }

        message.setCreatedAt(LocalDateTime.now());
        exchangeMessageRepository.save(message);
        return Result.success();
    }
}