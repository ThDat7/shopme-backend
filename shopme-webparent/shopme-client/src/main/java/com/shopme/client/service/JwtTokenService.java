package com.shopme.client.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.shopme.common.entity.Customer;

import java.text.ParseException;

public interface JwtTokenService {
    String generateToken(Customer customer);

    SignedJWT parseToken(String token) throws ParseException, JOSEException;
}
