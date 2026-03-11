package com.campus.idle.vo.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadVO {
    private String url;
    private String fileName;
}
