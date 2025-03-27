package com.shopme.client.service;

import com.shopme.client.dto.request.CustomerRegistrationRequest;
import com.shopme.client.dto.request.CustomerResendVerifyEmailRequest;
import com.shopme.client.dto.request.CustomerVerifyEmailRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.CustomerVerifyEmailResponse;
import com.shopme.common.entity.Customer;

public interface CustomerService {
    Customer customerFromGoogleLogin(String email, String name);

    AuthenticationResponse register(CustomerRegistrationRequest request);

    CustomerVerifyEmailResponse verify(CustomerVerifyEmailRequest request);

    void resendVerification(CustomerResendVerifyEmailRequest request);
}
