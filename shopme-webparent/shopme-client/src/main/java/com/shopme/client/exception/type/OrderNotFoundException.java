package com.shopme.client.exception.type;

public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException() {
        super("Không tìm thấy đơn hàng");
    }
}
