package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerInfoResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
}
