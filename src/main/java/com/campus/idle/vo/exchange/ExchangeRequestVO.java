package com.campus.idle.vo.exchange;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExchangeRequestVO {
    private Long id;

    private Long itemId;
    private String itemTitle;

    private Long fromUserId;
    private String fromUserNickname;

    private Long toUserId;
    private String toUserNickname;

    private String message;
    private String offeredItemDesc;

    private Integer status;

    private Integer ownerConfirmed;
    private Integer requesterConfirmed;

    private LocalDateTime handledTime;
    private LocalDateTime cancelledTime;
    private LocalDateTime createdAt;
}