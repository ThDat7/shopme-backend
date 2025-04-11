package com.shopme.client.exception.type;

public class PayOSGenerateException extends BusinessException {
    public PayOSGenerateException() {
        super("Không thể tạo mã thanh toán");
    }
}
