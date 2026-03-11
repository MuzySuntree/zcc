package com.campus.idle.vo.exchange;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExchangeRecordVO {
    private Long id;
    private Long requestId;
    private Long itemId;
    private String itemTitle;
    private Long ownerUserId;
    private String ownerNickname;
    private Long requesterUserId;
    private String requesterNickname;
    private LocalDateTime exchangeTime;
    private String exchangeLocation;
    private String note;
    private Integer status;
}
