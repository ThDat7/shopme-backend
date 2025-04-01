package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.ProvinceCreateRequest;
import com.shopme.admin.dto.request.ProvinceUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.ProvinceMapper;
import com.shopme.admin.mapper.UtilMapper;
import com.shopme.admin.repository.ProvinceRepository;
import com.shopme.admin.service.ProvinceService;
import com.shopme.common.entity.Province;
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
public class ProvinceServiceImpl implements ProvinceService {
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_COUNTRIES_PER_PAGE = 4;

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;
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
    public ListResponse<ProvinceListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", "");

        Page<Province> provincePage = provinceRepository.findAll(keyword, pageable);
        List<ProvinceListResponse> provinceListRespons = provincePage.getContent().stream()
                .map(provinceMapper::toProvinceListResponse)
                .collect(Collectors.toList());

        return ListResponse.<ProvinceListResponse>builder()
                .content(provinceListRespons)
                .totalPages(provincePage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public ProvinceDetailResponse createProvince(ProvinceCreateRequest request) {
        if (provinceRepository.existsByName(request.getName()))
            throw new RuntimeException("Province already exists");

        Province province = provinceMapper.toEntity(request);
        provinceRepository.save(province);
        return provinceMapper.toProvinceDetailResponse(province);
    }


    @Override
    @Transactional
    public ProvinceDetailResponse updateProvince(Integer id, ProvinceUpdateRequest request) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Province not found"));

        boolean isNameUnique = !provinceRepository.existsByNameAndIdNot(request.getName(), id);
        if (!isNameUnique)
            throw new RuntimeException("Province already exists");

        province.setName(request.getName());
        province.setCode(request.getCode());

        provinceRepository.save(province);
        return provinceMapper.toProvinceDetailResponse(province);
    }

    @Override
    public void deleteProvince(Integer id) {
        Province province = provinceRepository.findById(id).orElseThrow(() -> new RuntimeException("Province not found"));
        provinceRepository.delete(province);
    }

    @Override
    public ProvinceDetailResponse getProvinceById(Integer id) {
        Province province = provinceRepository.findById(id).orElseThrow(() -> new RuntimeException("Province not found"));
        return provinceMapper.toProvinceDetailResponse(province);
    }

    @Override
    public List<FormSelectResponse> listAllForFormSelection() {
        List<Province> provinces = provinceRepository.findAll();
        return provinces.stream()
                .map(i -> utilMapper.toFormSelectResponse(i.getId().toString(), i.getName()))
                .collect(Collectors.toList());
    }
}
