package com.shopme.client.service.impl;

import com.shopme.client.dto.response.FormSelectResponse;
import com.shopme.client.mapper.UtilMapper;
import com.shopme.client.repository.DistrictRepository;
import com.shopme.client.repository.ProvinceRepository;
import com.shopme.client.repository.WardRepository;
import com.shopme.client.service.LocationService;
import com.shopme.common.entity.District;
import com.shopme.common.entity.Province;
import com.shopme.common.entity.Ward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final UtilMapper utilMapper;

    @Override
    public List<FormSelectResponse> listProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return provinces.stream()
                .map(province -> utilMapper.toFormSelectResponse(province.getId().toString(), province.getName()))
                .toList();
    }

    @Override
    public List<FormSelectResponse> listDistrictsByProvinceId(Integer provinceId) {
        List<District> districts = districtRepository.findAllByProvinceId(provinceId);
        return districts.stream()
                .map(district -> utilMapper.toFormSelectResponse(district.getId().toString(), district.getName()))
                .toList();
    }

    @Override
    public List<FormSelectResponse> listWardsByDistrictId(Integer districtId) {
        List<Ward> wards = wardRepository.findAllByDistrictId(districtId);
        return wards.stream()
                .map(ward -> utilMapper.toFormSelectResponse(ward.getId().toString(), ward.getName()))
                .toList();
    }
}
