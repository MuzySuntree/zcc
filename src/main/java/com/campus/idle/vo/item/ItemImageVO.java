package com.campus.idle.vo.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemImageVO {
    private Long id;
    private String imageUrl;
    private Integer sortNo;
    private Integer isCover;
}
