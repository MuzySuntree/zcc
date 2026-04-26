package com.campus.idle.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResult<T> {
    private List<T> records;
    private long total;
    private int pageNum;
    private int pageSize;
    private int totalPages;
}
