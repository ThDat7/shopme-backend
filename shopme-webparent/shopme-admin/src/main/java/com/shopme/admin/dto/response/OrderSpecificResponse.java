package com.shopme.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSpecificResponse {
    private Integer productId;
    private String productName;
    private Integer quantity;
    private Float unitPrice;
    private Float shippingCost;
    private Float subtotal;
}

