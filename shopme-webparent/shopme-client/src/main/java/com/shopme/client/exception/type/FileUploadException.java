package com.shopme.client.exception.type;

public class FileUploadException extends RuntimeException {
    public FileUploadException() {
        super("Lỗi khi tải file lên");
    }
}
