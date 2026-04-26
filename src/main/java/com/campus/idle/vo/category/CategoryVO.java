package com.campus.idle.vo.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryVO {
    private Long id;
    private String categoryName;
    private Integer sortNo;
    private String icon;
    private Integer status;
}
