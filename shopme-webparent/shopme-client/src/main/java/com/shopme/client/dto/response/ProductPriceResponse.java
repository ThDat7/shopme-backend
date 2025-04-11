package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPriceResponse {
    protected int price;
    protected float discountPercent;
    protected int discountPrice;
}
