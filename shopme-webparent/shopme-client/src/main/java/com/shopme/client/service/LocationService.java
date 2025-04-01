package com.shopme.client.service;

import com.shopme.client.dto.response.FormSelectResponse;

import java.util.List;

public interface LocationService {
    List<FormSelectResponse> listProvinces();

    List<FormSelectResponse> listDistrictsByProvinceId(Integer provinceId);

    List<FormSelectResponse> listWardsByDistrictId(Integer districtId);
}
