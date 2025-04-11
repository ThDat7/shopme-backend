package com.shopme.client.exception.type;

public class CountryNotFoundException extends BusinessException {
    public CountryNotFoundException() {
        super("Không tìm thấy quốc gia.");
    }
}
