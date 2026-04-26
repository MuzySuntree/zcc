package com.campus.idle.controller;

import com.campus.idle.common.Result;
import com.campus.idle.service.FileService;
import com.campus.idle.vo.item.UploadVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class UploadController {

    private final FileService fileService;

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Result<UploadVO> upload(MultipartFile file) {
        return Result.success(fileService.uploadItemImage(file));
    }
}
