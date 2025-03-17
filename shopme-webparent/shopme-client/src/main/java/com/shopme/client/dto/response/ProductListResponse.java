package com.shopme.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private Integer id;
    private String name;
    private float price;
    private float discountPercent;
    private String mainImage;
}