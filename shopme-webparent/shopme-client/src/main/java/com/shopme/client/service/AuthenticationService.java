package com.shopme.client.service;

import com.shopme.client.dto.request.GoogleAuthenticationRequest;
import com.shopme.client.dto.request.IntrospectRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.IntrospectResponse;


public interface AuthenticationService {
    AuthenticationResponse googleAuthentication(GoogleAuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest introspectRequest);

}
