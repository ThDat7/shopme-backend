package com.shopme.client.service;

import com.shopme.client.dto.request.AddressRequest;
import com.shopme.client.dto.response.AddressDetailResponse;
import com.shopme.client.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    void deleteAddress(Integer id);

    AddressDetailResponse getDefaultAddress();

    void setDefaultAddress(Integer id);

    AddressDetailResponse updateAddress(Integer id, AddressRequest addressRequest);

    AddressDetailResponse createAddress(AddressRequest addressRequest);

    AddressDetailResponse getAddressDetail(Integer id);

    List<AddressResponse> getAll();
}
