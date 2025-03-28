package com.shopme.client.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.SignedJWT;
import com.shopme.client.dto.request.CustomerLoginRequest;
import com.shopme.client.dto.request.GoogleAuthenticationRequest;
import com.shopme.client.dto.request.IntrospectRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.IntrospectResponse;
import com.shopme.client.mapper.AuthenticationMapper;
import com.shopme.client.repository.CustomerRepository;
import com.shopme.client.service.AuthenticationService;
import com.shopme.client.service.CustomerService;
import com.shopme.common.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final JwtTokenServiceImpl jwtTokenService;
    private final CustomerRepository customerRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final AuthenticationMapper authenticationMapper;

    @Override
    public AuthenticationResponse googleAuthentication(GoogleAuthenticationRequest request) {
        String googleToken = request.getToken();
        GoogleIdToken idToken;
        try {
            idToken = googleIdTokenVerifier.verify(googleToken);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken == null)
            throw new RuntimeException("Invalid token");

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Customer customer = customerService.customerFromGoogleLogin(email, name);

        String token = jwtTokenService.generateToken(customer);

        return authenticationMapper.toAuthenticationResponse(token, customer);
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        try {
            String token = introspectRequest.getToken();
            SignedJWT signedJWT = jwtTokenService.parseToken(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isValid = expiryTime.after(new Date());
            return authenticationMapper.toIntrospectResponse(isValid);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthenticationResponse login(CustomerLoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), customer.getPassword());
        if (!authenticated)
            throw new RuntimeException("Invalid authenticate information");

        String token = jwtTokenService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
