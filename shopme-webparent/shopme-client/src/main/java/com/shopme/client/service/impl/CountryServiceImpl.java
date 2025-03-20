package com.shopme.client.service.impl;

import com.shopme.client.dto.response.FormSelectResponse;
import com.shopme.client.repository.CountryRepository;
import com.shopme.client.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<FormSelectResponse> listAllForFormSelection() {
        return countryRepository.findAll().stream()
                .map(country -> new FormSelectResponse(country.getId().toString(), country.getName()))
                .toList();
    }
}
