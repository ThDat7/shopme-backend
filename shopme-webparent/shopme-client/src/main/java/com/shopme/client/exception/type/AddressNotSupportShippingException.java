package com.shopme.client.exception.type;

public class AddressNotSupportShippingException extends BusinessException {
    public AddressNotSupportShippingException() {
        super("Địa chỉ này chưa hỗ trợ giao hàng");
    }
}
