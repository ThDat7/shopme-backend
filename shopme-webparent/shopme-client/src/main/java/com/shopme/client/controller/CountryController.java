package com.shopme.client.controller;

import com.shopme.client.dto.response.FormSelectResponse;
import com.shopme.client.service.CountryService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/form-select")
    public ApiResponse<List<FormSelectResponse>> listAllForFormSelection() {
        List<FormSelectResponse> countries = countryService.listAllForFormSelection();
        return ApiResponse.ok(countries);
    }
}
