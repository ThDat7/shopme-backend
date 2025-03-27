package com.shopme.client.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerVerifyEmailRequest {
    private String email;
    private String verificationCode;
}
