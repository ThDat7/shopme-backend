package com.shopme.client.exception.type;

public class UnAuthorizedException extends BusinessException {
    public UnAuthorizedException() {
        super("Bạn không có quyền truy cập vào trang này");
    }
}
