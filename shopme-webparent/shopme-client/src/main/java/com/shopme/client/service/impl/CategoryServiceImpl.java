package com.shopme.client.service.impl;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;
import com.shopme.client.mapper.CategoryMapper;
import com.shopme.client.repository.CategoryRepository;
import com.shopme.client.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Set<CategoryBreadcrumbResponse> findParentCategories(Integer id) {
        List<Object[]> categories = categoryRepository.findParentCategories(id);

        return categories.stream()
                .map(category -> categoryMapper
                        .toCategoryBreadcrumbResponse((Integer) category[0], (String) category[1]))
                .collect(Collectors.toSet());
    }

}
