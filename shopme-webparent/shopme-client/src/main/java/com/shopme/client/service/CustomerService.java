package com.shopme.client.service;

import com.shopme.client.dto.request.CustomerRegistrationRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.common.entity.Customer;

public interface CustomerService {
    Customer customerFromGoogleLogin(String email, String name);

    AuthenticationResponse register(CustomerRegistrationRequest request);
}
