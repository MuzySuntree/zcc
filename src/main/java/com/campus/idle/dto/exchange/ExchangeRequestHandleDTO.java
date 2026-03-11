package com.campus.idle.dto.exchange;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExchangeRequestHandleDTO {
    @NotBlank
    private String action;

    @Size(max = 255)
    private String exchangeLocation;

    @Size(max = 255)
    private String note;
}
