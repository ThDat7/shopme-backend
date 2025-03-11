package com.shopme.admin.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {

    private Integer id;
    private String password;
    private String firstName;
    private String lastName;
    private MultipartFile image;
}
