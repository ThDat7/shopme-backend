package com.shopme.client.exception.type;


public class OrderDetailNotFoundException extends BusinessException {
    public OrderDetailNotFoundException() {
        super("Không tìm thấy đơn hàng");
    }
}
