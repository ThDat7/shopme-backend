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
    private final String suffixBrandLogos;
    private final String suffixProductMainImages;
    private final String suffixProductImages;
    private final String suffixSiteLogo;

    public LocalFileUploadServiceImpl(
            @Value("${file.upload.host}") String host,
            @Value("${file.upload.suffix.user-photos}") String suffixUserPhotos,
            @Value("${file.upload.suffix.category-images}") String suffixCategoryImages,
            @Value("${file.upload.suffix.brand-logos}") String suffixBrandLogos,
            @Value("${file.upload.suffix.product-main-images}") String suffixProductMainImages,
            @Value("${file.upload.suffix.product-images}") String suffixProductImages,
            @Value("${file.upload.suffix.site-logo}") String suffixSiteLogo) {
        this.host = host;
        this.suffixUserPhotos = suffixUserPhotos;
        this.suffixCategoryImages = suffixCategoryImages;
        this.suffixBrandLogos = suffixBrandLogos;
        this.suffixProductMainImages = suffixProductMainImages;
        this.suffixProductImages = suffixProductImages;
        this.suffixSiteLogo = suffixSiteLogo;
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

    private void cleanDir(String dir) {
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


    @Override
    public String userPhotosUpload(MultipartFile file, Integer userId) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");

        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String uploadDir = getUserPhotosDir(userId);
        String fileName = file.getOriginalFilename();

        cleanDir(uploadDir);
        saveFile(uploadDir, fileName, file);
        return fileName;
    }

    @Override
    public String getUserPhotosURL(Integer userId, String photos) {
        return String.format("%s/%s/%s/%s", host, suffixUserPhotos, userId, photos);
    }

    private String getUserPhotosDir(Integer userId) {
        return suffixUserPhotos + "/" + userId;
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

    @Override
    public String getCategoryImageUrl(Integer categoryId, String image) {
        return String.format("%s/%s/%s/%s", host, "category-images", categoryId, image);
    }

    private String getCategoryImagesDir(Integer categoryId) {
        return suffixCategoryImages + "/" + categoryId;
    }


    @Override
    public String brandLogoUpload(MultipartFile file, Integer brandId) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");

        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String uploadDir = getBrandLogoDir(brandId);
        String fileName = file.getOriginalFilename();

        cleanDir(uploadDir);
        saveFile(uploadDir, fileName, file);
        return fileName;
    }

    @Override
    public String getBrandLogoUrl(Integer brandId, String image) {
        return String.format("%s/%s/%s/%s", host, "brand-logos", brandId, image);
    }

    private String getBrandLogoDir(Integer brandId) {
        return suffixBrandLogos + "/" + brandId;
    }




    @Override
    public String productMainImageUpload(MultipartFile file, Integer productId) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");

        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String uploadDir = getProductMainImageDir(productId);
        String fileName = file.getOriginalFilename();

        saveFile(uploadDir, fileName, file);
        return fileName;
    }

    @Override
    public String getProductMainImageUrl(Integer productId, String image) {
        return String.format("%s/%s/%s/%s", host, "product-images", productId, image);
    }

    private String getProductMainImageDir(Integer productId) {
        return suffixProductMainImages + "/" + productId;
    }

    @Override
    public String productImagesUpload(MultipartFile file, Integer productId) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");

        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String uploadDir = getProductImagesDir(productId);
        String fileName = file.getOriginalFilename();

        saveFile(uploadDir, fileName, file);
        return fileName;
    }

    @Override
    public String getProductImagesUrl(Integer productId, String image) {
        return String.format("%s/%s/%s/%s/%s", host, suffixProductMainImages, productId, suffixProductImages, image);
    }

    private String getProductImagesDir(Integer productId) {
        return String.format("%s/%s/%s", suffixProductMainImages, productId, suffixProductImages);
    }

    @Override
    public String getSiteLogoUrl(String fileName) {
        return String.format("%s/%s/%s", host, "site-logo", fileName);
    }

    @Override
    public String siteLogoUpload(MultipartFile file) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");

        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String uploadDir = suffixSiteLogo + "/";
        String fileName = file.getOriginalFilename();

        saveFile(uploadDir, fileName, file);
        return fileName;
    }

}
