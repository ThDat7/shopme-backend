package com.shopme.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private Integer id;
    private String recipientName;
    private String phoneNumber;
    private String address;
    private boolean defaultForShipping;
}
