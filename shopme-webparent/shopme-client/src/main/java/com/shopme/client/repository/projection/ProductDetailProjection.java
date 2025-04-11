package com.shopme.client.repository.projection;


public interface ProductDetailProjection {
    Integer getId();
    String getName();
    String getAlias();
    String getDescription();
    boolean isInStock();

    int getPrice();
    float getDiscountPercent();

    float getLength();
    float getWidth();
    float getHeight();
    float getWeight();

    String getMainImage();

    Integer getCategoryId();
    String getCategory();
    String getBrand();

    double getAverageRating();
    long getReviewCount();
    long getSaleCount();
}
