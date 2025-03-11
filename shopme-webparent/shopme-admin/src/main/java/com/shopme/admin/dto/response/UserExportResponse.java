package com.shopme.admin.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class UserExportResponse {
    private Integer id;
    private String photos;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private Boolean enabled;
}
