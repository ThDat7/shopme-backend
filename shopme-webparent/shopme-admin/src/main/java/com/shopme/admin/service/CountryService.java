package com.shopme.admin.service;

import com.shopme.admin.dto.request.CountryCreateRequest;
import com.shopme.admin.dto.request.CountryUpdateRequest;
import com.shopme.admin.dto.response.CountryDetailResponse;
import com.shopme.admin.dto.response.CountryListResponse;
import com.shopme.admin.dto.response.FormSelectResponse;
import com.shopme.admin.dto.response.ListResponse;

import java.util.List;
import java.util.Map;

public interface CountryService {
    ListResponse<CountryListResponse> listByPage(Map<String, String> params);

    CountryDetailResponse createCountry(CountryCreateRequest request);

    void deleteCountry(Integer id);

    CountryDetailResponse getCountryById(Integer id);

    CountryDetailResponse updateCountry(Integer id, CountryUpdateRequest request);

    List<FormSelectResponse> listAllForFormSelection();
}
