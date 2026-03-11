package com.campus.idle.dto.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ItemQueryDTO {
    private String keyword;
    private Long categoryId;
    private Integer status;

    @Min(1)
    private Integer pageNum = 1;

    @Min(1)
    @Max(50)
    private Integer pageSize = 10;
}
