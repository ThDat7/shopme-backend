package com.shopme.client.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerLoginRequest {
    private String email;
    private String password;
}
