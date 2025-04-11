package com.shopme.client.exception.type;

public class AddressNotFoundException extends BusinessException {
    public AddressNotFoundException() {
        super("Không tìm thấy địa chỉ.");
    }
}
