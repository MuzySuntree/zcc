package com.campus.idle.vo.item;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemSimpleVO {
    private Long id;
    private String title;
    private String categoryName;
    private String ownerNickname;
    private Integer conditionLevel;
    private Integer status;
    private String coverImage;
    private LocalDateTime createdAt;
}
