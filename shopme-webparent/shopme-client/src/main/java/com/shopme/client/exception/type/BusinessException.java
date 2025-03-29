package com.shopme.client.exception.type;

public class BusinessException extends RuntimeException {
//    private final int code;

    public BusinessException(String message) {
        super(message);
    }
}
