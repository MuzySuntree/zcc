package com.campus.idle.vo.item;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDetailVO {
    private Long id;
    private Long ownerId;
    private String ownerNickname;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String description;
    private Integer conditionLevel;
    private String expectedItemDesc;
    private String campusLocation;
    private String contactInfo;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private List<ItemImageVO> images;
}
