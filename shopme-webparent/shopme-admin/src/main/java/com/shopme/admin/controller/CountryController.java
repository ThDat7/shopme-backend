package com.shopme.admin.controller;


import com.shopme.admin.dto.request.CountryCreateRequest;
import com.shopme.admin.dto.request.CountryUpdateRequest;
import com.shopme.admin.dto.response.CountryDetailResponse;
import com.shopme.admin.dto.response.CountryListResponse;
import com.shopme.admin.dto.response.FormSelectResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.service.CountryService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    public ApiResponse<ListResponse<CountryListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<CountryListResponse> listResponse = countryService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @PostMapping
    public ApiResponse<CountryDetailResponse> createCountry(@RequestBody CountryCreateRequest request) {
        return ApiResponse.ok(countryService.createCountry(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteCountry(@PathVariable Integer id) {
        countryService.deleteCountry(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<CountryDetailResponse> getCountry(@PathVariable Integer id) {
        CountryDetailResponse country = countryService.getCountryById(id);
        return ApiResponse.ok(country);
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<CountryDetailResponse> updateCountry(@PathVariable Integer id,
                                                        @RequestBody CountryUpdateRequest request) {
        return ApiResponse.ok(countryService.updateCountry(id, request));
    }

    @GetMapping("/form-select")
    public ApiResponse<List<FormSelectResponse>> listAllForFormSelection() {
        List<FormSelectResponse> countries = countryService.listAllForFormSelection();
        return ApiResponse.ok(countries);
    }
}
