package com.shopme.client.controller;

import com.shopme.client.dto.request.CustomerUpdateProfileRequest;
import com.shopme.client.dto.request.CustomerRegistrationRequest;
import com.shopme.client.dto.request.CustomerResendVerifyEmailRequest;
import com.shopme.client.dto.request.CustomerVerifyEmailRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.CustomerInfoResponse;
import com.shopme.client.dto.response.CustomerVerifyEmailResponse;
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

    @PostMapping("/verify")
    public ApiResponse<CustomerVerifyEmailResponse> verify(@RequestBody CustomerVerifyEmailRequest request) {
        return ApiResponse.ok(customerService.verify(request));
    }

    @PostMapping("/resend-verification")
    public ApiResponse<Void> resendVerification(@RequestBody CustomerResendVerifyEmailRequest request) {
        customerService.resendVerification(request);
        return ApiResponse.ok();
    }

    @GetMapping("/profile")
    public ApiResponse<CustomerInfoResponse> getProfile() {
        return ApiResponse.ok(customerService.getProfile());
    }

    @PutMapping("/profile")
    public ApiResponse<CustomerInfoResponse> updateProfile(@RequestBody CustomerUpdateProfileRequest request) {
        return ApiResponse.ok(customerService.updateProfile(request));
    }

}
