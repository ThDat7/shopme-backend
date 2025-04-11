package com.shopme.client.exception.type;

public class InvalidAuthenticationException extends BusinessException {
    public InvalidAuthenticationException() {
        super("Sai thông tin đăng nhập");
    }
}
