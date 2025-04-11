package com.shopme.client.exception.type;

public class TokenInvalidException extends BusinessException {
    public TokenInvalidException() {
        super("Thông tin xác thực không hợp lệ");
    }
}
