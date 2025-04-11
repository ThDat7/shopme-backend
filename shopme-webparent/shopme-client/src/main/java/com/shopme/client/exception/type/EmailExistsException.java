package com.shopme.client.exception.type;

public class EmailExistsException extends BusinessException {
    public EmailExistsException() {
        super("Email đã tồn tại.");
    }
}
