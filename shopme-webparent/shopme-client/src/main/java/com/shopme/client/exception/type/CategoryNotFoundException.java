package com.shopme.client.exception.type;

public class CategoryNotFoundException extends BusinessException {
    public CategoryNotFoundException() {
        super("Không tìm thấy danh mục");
    }
}
