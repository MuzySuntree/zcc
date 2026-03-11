package com.campus.idle.dto.item;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ItemUpdateDTO {
    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer conditionLevel;

    @Size(max = 255)
    private String expectedItemDesc;

    @Size(max = 100)
    private String campusLocation;

    @Size(max = 100)
    private String contactInfo;

    @NotEmpty
    @Size(max = 9)
    private List<@NotBlank String> imageUrls;
}
