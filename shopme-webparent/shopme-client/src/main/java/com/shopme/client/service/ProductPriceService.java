package com.shopme.client.service;

import com.shopme.client.dto.response.ProductPriceResponse;
import com.shopme.common.entity.Product;

public interface ProductPriceService {
    int calculateFinalPrice(Product product);
    ProductPriceResponse calculateProductPriceResponse(Integer productId);
}
