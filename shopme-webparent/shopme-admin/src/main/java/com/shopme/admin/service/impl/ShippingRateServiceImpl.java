package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.ShippingRateCreateRequest;
import com.shopme.admin.dto.request.ShippingRateUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.ShippingRateMapper;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.repository.ShippingRateRepository;
import com.shopme.admin.service.ShippingRateService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingRateServiceImpl implements ShippingRateService {
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_SR_PER_PAGE = 4;

    private final ShippingRateRepository shippingRateRepository;
    private final CountryRepository countryRepository;
    private final ShippingRateMapper shippingRateMapper;

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_SR_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<ShippingRateListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", "");

        Page<ShippingRate> shippingRatePage = shippingRateRepository.findAll(keyword, pageable);
        List<ShippingRateListResponse> shippingRateListResponses = shippingRatePage.getContent().stream()
                .map(shippingRateMapper::toShippingRateListResponse)
                .collect(Collectors.toList());

        return ListResponse.<ShippingRateListResponse>builder()
                .content(shippingRateListResponses)
                .totalPages(shippingRatePage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public ShippingRateDetailResponse createShippingRate(ShippingRateCreateRequest request) {
        ShippingRate shippingRate = shippingRateMapper.toEntity(request);

        // Set country
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        shippingRate.setCountry(country);

        shippingRateRepository.save(shippingRate);
        return shippingRateMapper.toShippingRateDetailResponse(shippingRate);
    }


    @Override
    @Transactional
    public ShippingRateDetailResponse updateShippingRate(Integer id, ShippingRateUpdateRequest request) {
        ShippingRate shippingRate = shippingRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShippingRate not found"));

        shippingRate.setRate(request.getRate());
        shippingRate.setState(request.getState());
        shippingRate.setDays(request.getDays());
        shippingRate.setCodSupported(request.isCodSupported());

        // Set country
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        shippingRate.setCountry(country);

        shippingRateRepository.save(shippingRate);
        return shippingRateMapper.toShippingRateDetailResponse(shippingRate);
    }

    @Override
    public void deleteShippingRate(Integer id) {
        ShippingRate shippingRate = shippingRateRepository.findById(id).orElseThrow(() -> new RuntimeException("ShippingRate not found"));
        shippingRateRepository.delete(shippingRate);
    }

    @Override
    public ShippingRateDetailResponse getShippingRateById(Integer id) {
        ShippingRate shippingRate = shippingRateRepository.findById(id).orElseThrow(() -> new RuntimeException("ShippingRate not found"));
        return shippingRateMapper.toShippingRateDetailResponse(shippingRate);
    }
}
