package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingAddressResponse {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
