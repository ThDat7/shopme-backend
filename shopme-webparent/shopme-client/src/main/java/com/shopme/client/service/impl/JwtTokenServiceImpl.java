package com.shopme.client.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shopme.client.exception.type.GenerateTokenException;
import com.shopme.client.exception.type.TokenInvalidException;
import com.shopme.client.service.JwtTokenService;
import com.shopme.common.entity.Customer;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Setter
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Override
    public String generateToken(Customer customer) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(customer.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(100, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customerId", customer.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new GenerateTokenException();
        }
    }

    @Override
    public SignedJWT parseToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        if (signedJWT.verify(verifier)) {
            return signedJWT;
        } else {
            throw new TokenInvalidException();
        }
    }


}
