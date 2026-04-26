package com.campus.idle.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateDTO {
    @NotBlank
    @Size(max = 50)
    private String categoryName;

    @NotNull
    private Integer sortNo;

    @Size(max = 255)
    private String icon;

    private Integer status = 1;
}
