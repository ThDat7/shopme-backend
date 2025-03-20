package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemResponse {
    private Integer productId;
    private String name;
    private float price;
    private float discount;
    private String mainImage;
    private Integer quantity;
}
