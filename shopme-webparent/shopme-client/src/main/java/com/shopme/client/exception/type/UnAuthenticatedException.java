package com.shopme.client.exception.type;

public class UnAuthenticatedException extends BusinessException {
    public UnAuthenticatedException() {
        super("Bạn chưa đăng nhập");
    }
}
