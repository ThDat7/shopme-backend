package com.shopme.client.service;

import com.shopme.client.dto.request.GoogleAuthenticationRequest;
import com.shopme.client.dto.request.IntrospectRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.IntrospectResponse;
import com.shopme.common.entity.Customer;
import org.springframework.security.oauth2.core.user.OAuth2User;


public interface AuthenticationService {
    AuthenticationResponse googleAuthentication(GoogleAuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest introspectRequest);

    Customer getCurrentCustomer();

    Integer getCurrentCustomerId();
}
