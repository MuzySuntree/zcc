package com.campus.idle.dto.exchange;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExchangeRequestCreateDTO {
    @NotNull
    private Long itemId;

    @Size(max = 255)
    private String message;

    @Size(max = 255)
    private String offeredItemDesc;
}
