package com.shopme.client.exception.type;

public class PromotionNotFoundException extends BusinessException {
    public PromotionNotFoundException() {
        super("Không tìm thấy khuyến mãi");
    }
}
