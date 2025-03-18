package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.CountryCreateRequest;
import com.shopme.admin.dto.request.CountryUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.CountryMapper;
import com.shopme.admin.mapper.UtilMapper;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.service.CountryService;
import com.shopme.common.entity.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_COUNTRIES_PER_PAGE = 4;

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final UtilMapper utilMapper;

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_COUNTRIES_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<CountryListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", "");

        Page<Country> countryPage = countryRepository.findAll(keyword, pageable);
        List<CountryListResponse> countryListResponses = countryPage.getContent().stream()
                .map(countryMapper::toCountryListResponse)
                .collect(Collectors.toList());

        return ListResponse.<CountryListResponse>builder()
                .content(countryListResponses)
                .totalPages(countryPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public CountryDetailResponse createCountry(CountryCreateRequest request) {
        if (countryRepository.existsByName(request.getName()))
            throw new RuntimeException("Country already exists");

        Country country = countryMapper.toEntity(request);
        return countryMapper.toCountryDetailResponse(country);
    }


    @Override
    @Transactional
    public CountryDetailResponse updateCountry(Integer id, CountryUpdateRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        boolean isNameUnique = !countryRepository.existsByNameAndIdNot(request.getName(), id);
        if (!isNameUnique)
            throw new RuntimeException("Country already exists");

        country.setName(request.getName());
        country.setCode(request.getCode());

        countryRepository.save(country);
        return countryMapper.toCountryDetailResponse(country);
    }

    @Override
    public void deleteCountry(Integer id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("Country not found"));
        countryRepository.delete(country);
    }

    @Override
    public CountryDetailResponse getCountryById(Integer id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("Country not found"));
        return countryMapper.toCountryDetailResponse(country);
    }

    @Override
    public List<FormSelectResponse> listAllForFormSelection() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(i -> utilMapper.toFormSelectResponse(i.getId().toString(), i.getName()))
                .collect(Collectors.toList());
    }
}
