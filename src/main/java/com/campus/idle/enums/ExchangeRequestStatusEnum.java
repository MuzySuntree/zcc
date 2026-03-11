package com.campus.idle.enums;

import lombok.Getter;

@Getter
public enum ExchangeRequestStatusEnum {
    PENDING(1, "待处理"),
    AGREED(2, "已同意"),
    REJECTED(3, "已拒绝"),
    CANCELED(4, "已取消");

    private final int code;
    private final String desc;

    ExchangeRequestStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
