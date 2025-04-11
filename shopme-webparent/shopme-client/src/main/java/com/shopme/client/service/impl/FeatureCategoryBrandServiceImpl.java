package com.shopme.client.service.impl;

import com.shopme.client.dto.response.FeatureCategoryBrandResponse;
import com.shopme.client.mapper.FeatureCategoryBrandMapper;
import com.shopme.client.repository.FeatureCategoryBrandRepository;
import com.shopme.client.service.FeatureCategoryBrandService;
import com.shopme.common.entity.FeatureCategoryBrand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureCategoryBrandServiceImpl implements FeatureCategoryBrandService {
    private final FeatureCategoryBrandRepository featureCategoryBrandRepository;
    private final FeatureCategoryBrandMapper featureCategoryBrandMapper;

    private String getIcon(FeatureCategoryBrand featureCategoryBrand) {
        return "";
    }

    @Override
    public List<FeatureCategoryBrandResponse> getAll() {
        return featureCategoryBrandRepository.findAll().stream()
                .map(feature -> {
                    FeatureCategoryBrandResponse response = featureCategoryBrandMapper.toFeatureCategoryBrandResponse(feature);
                    response.setIcon(getIcon(feature));
                    return response;
                })
                .toList();
    }
}
