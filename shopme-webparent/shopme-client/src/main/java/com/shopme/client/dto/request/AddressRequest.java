package com.shopme.client.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String addressLine;
    private Integer wardId;
    private boolean defaultForShipping;
}
