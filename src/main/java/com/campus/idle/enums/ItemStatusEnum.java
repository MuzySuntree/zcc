package com.campus.idle.enums;

import lombok.Getter;

@Getter
public enum ItemStatusEnum {
    ON_SHELF(1, "上架"),
    OFF_SHELF(2, "下架"),
    EXCHANGED(3, "已交换");

    private final int code;
    private final String desc;

    ItemStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
