package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemResponse {
    private Integer productId;
    private String name;
    private int price;
    private float discountPercent;
    private int discountPrice;
    private String mainImage;
    private Integer quantity;
}
