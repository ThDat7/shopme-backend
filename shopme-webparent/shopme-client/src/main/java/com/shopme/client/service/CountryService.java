package com.shopme.client.service;

import com.shopme.client.dto.response.FormSelectResponse;

import java.util.List;

public interface CountryService {
    List<FormSelectResponse> listAllForFormSelection();
}
