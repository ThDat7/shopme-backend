package com.shopme.client.exception.type;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException() {
        super("Không tìm thấy sản phẩm");
    }
}
