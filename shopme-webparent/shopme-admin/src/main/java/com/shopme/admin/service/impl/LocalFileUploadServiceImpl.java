package com.shopme.admin.service.impl;

import com.shopme.admin.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalFileUploadServiceImpl implements FileUploadService {
    private final String host;
    private final String suffixUserPhotos;
    private final String suffixCategoryImages;

    public LocalFileUploadServiceImpl(
            @Value("${file.upload.host}") String host,
            @Value("${file.upload.suffix.user-photos}") String suffixUserPhotos,
            @Value("${file.upload.suffix.category-images}") String suffixCategoryImages) {
        this.host = host;
        this.suffixUserPhotos = suffixUserPhotos;
        this.suffixCategoryImages = suffixCategoryImages;
    }

    public String getUserPhotosURL(Integer userId, String photos) {
        return String.format("%s/%s/%s/%s", host, suffixUserPhotos, userId, photos);
    }

    @Override
    public String getCategoryImageUrl(Integer categoryId, String image) {
        return String.format("%s/%s/%s/%s", host, "category-images", categoryId, image);
    }

    @Override
    public String categoryImageUpload(MultipartFile file, Integer categoryId) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");

        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String uploadDir = getCategoryImagesDir(categoryId);
        String fileName = file.getOriginalFilename();

        cleanDir(uploadDir);
        saveFile(uploadDir, fileName, file);
        return fileName;
    }

    private String getUserPhotosDir(Integer userId) {
        return suffixUserPhotos + "/" + userId;
    }

    private String getCategoryImagesDir(Integer categoryId) {
        return suffixCategoryImages + "/" + categoryId;
    }

    private void saveFile(String uploadDir, String fileName,
                          MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Could not save file: " + fileName, ex);
        }
    }

    public void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        System.out.println("Could not delete file: " + file);
                    }
                }
            });
        } catch (IOException ex) {
            System.out.println("Could not list directory: " + dirPath);
        }
    }

    public String userPhotosUpload(MultipartFile file, Integer userId) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");

        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String uploadDir = getUserPhotosDir(userId);
        String fileName = file.getOriginalFilename();

        cleanDir(uploadDir);
        saveFile(uploadDir, fileName, file);
        return fileName;
    }

}
