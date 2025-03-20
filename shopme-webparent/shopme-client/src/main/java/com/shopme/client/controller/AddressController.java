package com.shopme.client.controller;

import com.shopme.client.dto.request.AddressRequest;
import com.shopme.client.dto.response.AddressDetailResponse;
import com.shopme.client.dto.response.AddressResponse;
import com.shopme.client.service.AddressService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ApiResponse<List<AddressResponse>> getAll() {
        return ApiResponse.ok(addressService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<AddressDetailResponse> getAddressDetail(@PathVariable Integer id) {
        return ApiResponse.ok(addressService.getAddressDetail(id));
    }

    @PostMapping
    public ApiResponse<AddressDetailResponse> createAddress(@RequestBody AddressRequest addressRequest) {
        return ApiResponse.ok(addressService.createAddress(addressRequest));
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressDetailResponse> updateAddress(@PathVariable Integer id,
                                                            @RequestBody AddressRequest addressRequest) {
        return ApiResponse.ok(addressService.updateAddress(id, addressRequest));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/default")
    public ApiResponse<AddressDetailResponse> getDefaultAddress() {
        return ApiResponse.ok(addressService.getDefaultAddress());
    }

    @PutMapping("/default/{id}")
    public ApiResponse<Void> setDefaultAddress(@PathVariable Integer id) {
        addressService.setDefaultAddress(id);
        return ApiResponse.ok(null);
    }
}
