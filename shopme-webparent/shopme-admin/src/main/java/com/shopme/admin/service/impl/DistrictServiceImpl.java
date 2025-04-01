package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.DistrictCreateRequest;
import com.shopme.admin.dto.request.DistrictUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.DistrictMapper;
import com.shopme.admin.mapper.UtilMapper;
import com.shopme.admin.repository.ProvinceRepository;
import com.shopme.admin.repository.DistrictRepository;
import com.shopme.admin.service.DistrictService;
import com.shopme.common.entity.District;
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
public class DistrictServiceImpl implements DistrictService {
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_STATES_PER_PAGE = 4;

    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictMapper districtMapper;
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
    public ListResponse<DistrictListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", "");
        Integer provinceId = params.containsKey("provinceId") ? Integer.parseInt(params.get("provinceId")) : null;

        Page<District> districtPage = districtRepository.findAll(keyword, provinceId, pageable);
        List<DistrictListResponse> districtListResponses = districtPage.getContent().stream()
                .map(districtMapper::toDistrictListResponse)
                .collect(Collectors.toList());

        return ListResponse.<DistrictListResponse>builder()
                .content(districtListResponses)
                .totalPages(districtPage.getTotalPages())
                .build();
    }

    @Override
    public List<FormSelectResponse> listForFormSelectionByProvinceId(Integer provinceId) {
        List<District> districts = districtRepository.findAllByProvinceId(provinceId);
        return districts.stream()
                .map(d -> new FormSelectResponse(d.getId().toString(), d.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DistrictDetailResponse createDistrict(DistrictCreateRequest request) {
        if (districtRepository.existsByName(request.getName()))
            throw new RuntimeException("District already exists");

        District district = districtMapper.toEntity(request);

        // Set province
        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new RuntimeException("Province not found"));
        district.setProvince(province);
        districtRepository.save(district);

        return districtMapper.toDistrictDetailResponse(district);
    }


    @Override
    @Transactional
    public DistrictDetailResponse updateDistrict(Integer id, DistrictUpdateRequest request) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found"));

        boolean isNameUnique = !districtRepository.existsByNameAndIdNot(request.getName(), id);
        if (!isNameUnique)
            throw new RuntimeException("District already exists");

        district.setName(request.getName());

        // Set categories
        Province province = provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new RuntimeException("Province not found"));
        district.setProvince(province);

        districtRepository.save(district);
        return districtMapper.toDistrictDetailResponse(district);
    }

    @Override
    public void deleteDistrict(Integer id) {
        District district = districtRepository.findById(id).orElseThrow(() -> new RuntimeException("District not found"));
        districtRepository.delete(district);
    }

    @Override
    public DistrictDetailResponse getDistrictById(Integer id) {
        District district = districtRepository.findById(id).orElseThrow(() -> new RuntimeException("District not found"));
        return districtMapper.toDistrictDetailResponse(district);
    }

}
