package com.shopme.client.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final AuthenticationMapper authenticationMapper;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

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

        String token = generateToken(customer);

        return authenticationMapper.toAuthenticationResponse(token, customer);
    }

    private String generateToken(Customer customer) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(customer.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customerId", customer.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        try {
            String token = introspectRequest.getToken();
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean verified = signedJWT.verify(verifier);
            boolean isValid = verified && expiryTime.after(new Date());
            return authenticationMapper.toIntrospectResponse(isValid);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getCurrentCustomer() {
        Integer customerId = getCurrentCustomerId();
        return customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Integer getCurrentCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long customerId = jwt.getClaim("customerId");
            return customerId.intValue();
        } catch (RuntimeException e) {
            throw new RuntimeException("Unauthorized");
        }
    }
}
