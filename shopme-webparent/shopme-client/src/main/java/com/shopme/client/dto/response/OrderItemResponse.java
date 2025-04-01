package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemResponse {
    private Integer id;
    private Integer productId;
    private String productName;
    private String productMainImage;
    private int quantity;
    private int unitPrice;
}
