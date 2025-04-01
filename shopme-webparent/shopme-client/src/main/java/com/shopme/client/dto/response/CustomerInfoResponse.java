package com.shopme.client.dto.response;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.CustomerStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerInfoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private CustomerStatus status;
    private AuthenticationType authenticationType;
}
