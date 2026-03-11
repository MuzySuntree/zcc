package com.campus.idle.enums;

import lombok.Getter;

@Getter
public enum ExchangeRecordStatusEnum {
    VALID(1, "有效"),
    INVALID(0, "作废");

    private final int code;
    private final String desc;

    ExchangeRecordStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
