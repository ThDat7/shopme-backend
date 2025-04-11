package com.shopme.client.exception.type;

public class CustomerAlreadyVerifiedException extends BusinessException {
    public CustomerAlreadyVerifiedException() {
        super("Khách hàng đã được xác minh trước đó");
    }
}
