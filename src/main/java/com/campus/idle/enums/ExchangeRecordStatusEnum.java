package com.campus.idle.enums;

import lombok.Getter;

@Getter
public enum ExchangeRecordStatusEnum {
    PROCESSING(1, "交换中"),
    COMPLETED(2, "已完成"),
    CANCELED(3, "已取消");

    private final int code;
    private final String desc;

    ExchangeRecordStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}