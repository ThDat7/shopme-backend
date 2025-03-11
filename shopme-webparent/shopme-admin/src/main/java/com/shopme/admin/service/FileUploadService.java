package com.shopme.admin.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String userPhotosUpload(MultipartFile file, Integer userId);

    String getUserPhotosURL(Integer userId, String photos);
}
