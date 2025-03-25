package com.shopme.client.controller;

import com.shopme.client.dto.request.CustomerRegistrationRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.service.CustomerService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(@RequestBody CustomerRegistrationRequest request) {
        return ApiResponse.ok(customerService.register(request));
    }

}
