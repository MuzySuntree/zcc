package com.campus.idle.service.impl;

import com.campus.idle.common.ResultCode;
import com.campus.idle.exception.BizException;
import com.campus.idle.service.FileService;
import com.campus.idle.vo.item.UploadVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public UploadVO uploadItemImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST, "文件不能为空");
        }
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext == null ? "" : "." + ext);
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return UploadVO.builder()
                    .url("/uploads/" + filename)
                    .fileName(filename)
                    .build();
        } catch (IOException e) {
            throw new BizException(ResultCode.ERROR, "上传失败: " + e.getMessage());
        }
    }
}
