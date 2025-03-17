package com.shopme.client.service;


public interface FileUploadService {
    String getCategoryImageUrl(Integer categoryId, String image);

    String getBrandLogoUrl(Integer brandId, String image);

    String getProductMainImageUrl(Integer productId, String image);

    String getProductImagesUrl(Integer productId, String image);
}
