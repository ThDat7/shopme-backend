package com.shopme.client.exception.type;

public class CustomerNotFoundException extends BusinessException {
    public CustomerNotFoundException() {
        super("Không tìm thấy khách hàng");
    }
}
