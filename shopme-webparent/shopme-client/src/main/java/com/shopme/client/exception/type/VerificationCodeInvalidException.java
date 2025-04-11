package com.shopme.client.exception.type;

public class VerificationCodeInvalidException extends BusinessException {
    public VerificationCodeInvalidException() {
        super("Mã xác minh không hợp lệ");
    }
}
