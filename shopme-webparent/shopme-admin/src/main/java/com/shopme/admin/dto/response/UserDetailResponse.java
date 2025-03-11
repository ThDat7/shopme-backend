package com.shopme.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Integer> roleIds;
    private Boolean enabled;
    private String photos;
}
