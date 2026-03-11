package com.campus.idle.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data, Instant.now().toEpochMilli());
    }

    public static Result<Void> success() {
        return new Result<>(200, "success", null, Instant.now().toEpochMilli());
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null, Instant.now().toEpochMilli());
    }
}
