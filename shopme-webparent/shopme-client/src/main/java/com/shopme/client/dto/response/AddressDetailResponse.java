package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDetailResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String addressLine;
    private Integer wardId;
    private boolean defaultForShipping;
}
