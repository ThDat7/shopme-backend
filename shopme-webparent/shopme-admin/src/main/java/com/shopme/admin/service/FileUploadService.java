package com.shopme.admin.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String userPhotosUpload(MultipartFile file, Integer userId);

    String getUserPhotosURL(Integer userId, String photos);

    String getCategoryImageUrl(Integer categoryId, String image);

    String categoryImageUpload(MultipartFile file, Integer categoryId);

    String getBrandLogoUrl(Integer brandId, String image);

    String brandLogoUpload(MultipartFile file, Integer brandId);
}
