package com.shopme.admin.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Integer> roleIds;
    private Boolean enabled;
}