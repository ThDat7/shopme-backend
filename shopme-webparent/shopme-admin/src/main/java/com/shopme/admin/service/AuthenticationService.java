package com.shopme.admin.service;

import com.nimbusds.jose.JOSEException;
import com.shopme.admin.dto.request.AuthenticationRequest;
import com.shopme.admin.dto.request.IntrospectRequest;
import com.shopme.admin.dto.response.AuthenticationResponse;
import com.shopme.admin.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    AuthenticationResponse authenticate(AuthenticationRequest authRequest);
}