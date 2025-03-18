package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.StateCreateRequest;
import com.shopme.admin.dto.request.StateUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.StateMapper;
import com.shopme.admin.mapper.UtilMapper;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.repository.StateRepository;
import com.shopme.admin.service.StateService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
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
public class StateServiceImpl implements StateService {
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_STATES_PER_PAGE = 4;

    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final StateMapper stateMapper;
    private final UtilMapper utilMapper;

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_STATES_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<StateListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", "");
        Integer countryId = params.containsKey("countryId") ? Integer.parseInt(params.get("countryId")) : null;

        Page<State> statePage = stateRepository.findAll(keyword, countryId, pageable);
        List<StateListResponse> stateListResponses = statePage.getContent().stream()
                .map(stateMapper::toStateListResponse)
                .collect(Collectors.toList());

        return ListResponse.<StateListResponse>builder()
                .content(stateListResponses)
                .totalPages(statePage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public StateDetailResponse createState(StateCreateRequest request) {
        if (stateRepository.existsByName(request.getName()))
            throw new RuntimeException("State already exists");

        State state = stateMapper.toEntity(request);

        // Set country
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        state.setCountry(country);

        return stateMapper.toStateDetailResponse(state);
    }


    @Override
    @Transactional
    public StateDetailResponse updateState(Integer id, StateUpdateRequest request) {
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found"));

        boolean isNameUnique = !stateRepository.existsByNameAndIdNot(request.getName(), id);
        if (!isNameUnique)
            throw new RuntimeException("State already exists");

        state.setName(request.getName());

        // Set categories
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        state.setCountry(country);

        stateRepository.save(state);
        return stateMapper.toStateDetailResponse(state);
    }

    @Override
    public void deleteState(Integer id) {
        State state = stateRepository.findById(id).orElseThrow(() -> new RuntimeException("State not found"));
        stateRepository.delete(state);
    }

    @Override
    public StateDetailResponse getStateById(Integer id) {
        State state = stateRepository.findById(id).orElseThrow(() -> new RuntimeException("State not found"));
        return stateMapper.toStateDetailResponse(state);
    }

}
