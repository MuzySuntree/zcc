package com.campus.idle.dto.exchange;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ExchangeQueryDTO {
    @Min(1)
    private Integer pageNum = 1;

    @Min(1)
    @Max(50)
    private Integer pageSize = 10;
}
