package com.shopme.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListResponse {
    private Integer id;
    private String name;
    private String mainImage;

    private int price;
    private float discountPercent;
    private int discountPrice;

    private float averageRating;
    private int reviewCount;
    private int saleCount;
}