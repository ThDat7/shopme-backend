package com.shopme.client.service.impl;

import com.shopme.client.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocalFileUploadServiceImpl implements FileUploadService {
    private final String host;
    private final String suffixCategoryImages;
    private final String suffixBrandLogos;
    private final String suffixProductMainImages;
    private final String suffixProductImages;

    public LocalFileUploadServiceImpl(
            @Value("${file.upload.host}") String host,
            @Value("${file.upload.suffix.category-images}") String suffixCategoryImages,
            @Value("${file.upload.suffix.brand-logos}") String suffixBrandLogos,
            @Value("${file.upload.suffix.product-main-images}") String suffixProductMainImages,
            @Value("${file.upload.suffix.product-images}") String suffixProductImages) {
        this.host = host;
        this.suffixCategoryImages = suffixCategoryImages;
        this.suffixBrandLogos = suffixBrandLogos;
        this.suffixProductMainImages = suffixProductMainImages;
        this.suffixProductImages = suffixProductImages;
    }

    @Override
    public String getCategoryImageUrl(Integer categoryId, String image) {
        return String.format("%s/%s/%s/%s", host, "category-images", categoryId, image);
    }

    @Override
    public String getBrandLogoUrl(Integer brandId, String image) {
        return String.format("%s/%s/%s/%s", host, "brand-logos", brandId, image);
    }

    @Override
    public String getProductMainImageUrl(Integer productId, String image) {
        return String.format("%s/%s/%s/%s", host, "product-images", productId, image);
    }

    @Override
    public String getProductImagesUrl(Integer productId, String image) {
        return String.format("%s/%s/%s/%s/%s", host, suffixProductMainImages, productId, suffixProductImages, image);
    }

    @Override
    public String getCarouselImageUrl(Integer carouselId, String image) {
        return String.format("%s/%s/%s/%s", host, "carousel-images", carouselId, image);
    }
}
