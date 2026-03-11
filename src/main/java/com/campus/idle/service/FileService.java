package com.campus.idle.service;

import com.campus.idle.vo.item.UploadVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UploadVO uploadItemImage(MultipartFile file);
}
