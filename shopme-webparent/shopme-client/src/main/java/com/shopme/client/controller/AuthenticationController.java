package com.shopme.client.controller;

import com.shopme.client.dto.request.GoogleAuthenticationRequest;
import com.shopme.client.dto.request.IntrospectRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.service.AuthenticationService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/google")
    public ApiResponse<AuthenticationResponse> googleAuthentication(@RequestBody GoogleAuthenticationRequest request) {
        return ApiResponse.ok(authenticationService.googleAuthentication(request));
    }

    @PostMapping("/introspect")
    public ApiResponse<?> introspect(@RequestBody IntrospectRequest introspectRequest) {
        return ApiResponse.ok(authenticationService.introspect(introspectRequest));
    }
}
