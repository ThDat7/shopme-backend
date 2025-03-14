package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.CategoryCreateRequest;
import com.shopme.admin.dto.request.CategoryUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.CategoryMapper;
import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.service.FileUploadService;
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
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_USERS_PER_PAGE = 4;

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileUploadService fileUploadService;

    private String getCategoryImageURL(Category category) {
        return fileUploadService.getCategoryImageUrl(category.getId(), category.getImage());
    }

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_USERS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<CategoryListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);

        Page<Category> categoryPage = categoryRepository.findRootCategories(pageable);
        List<CategoryListResponse> categoryListResponses = categoryPage.getContent().stream()
                .map(category -> {
                    boolean hasChildren = categoryRepository.hasChildren(category.getId());
                    var categoryDto = categoryMapper.toCategoryListResponse(category, hasChildren);
                    categoryDto.setImage(getCategoryImageURL(category));
                    return categoryDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<CategoryListResponse>builder()
                .content(categoryListResponses)
                .totalPages(categoryPage.getTotalPages())
                .build();
    }

    @Override
    public ListResponse<CategorySearchResponse> search(Map<String, String> params) {
        String keyword = params.get("keyword");
        if (keyword == null)
            throw new IllegalArgumentException("Keyword cannot be null");

        Pageable pageable = getPageableFromParams(params);

        Page<Category> categoryPage = categoryRepository.findAll(keyword, pageable);
        List<CategorySearchResponse> categorySearchResponses = categoryPage.getContent().stream()
                .map(category -> {
                    String breadcrumb = categoryRepository.findBreadcrumbById(category.getId());
                    var categoryDto = categoryMapper.toCategorySearchResponse(category, breadcrumb);
                    categoryDto.setImage(getCategoryImageURL(category));
                    return categoryDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<CategorySearchResponse>builder()
                .content(categorySearchResponses)
                .totalPages(categoryPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public CategoryDetailResponse createCategory(CategoryCreateRequest request) {

        if (request.getImage() == null || request.getImage().isEmpty())
            throw new IllegalArgumentException("Category image is required");

        if (categoryRepository.existsByName(request.getName()))
            throw new RuntimeException("Category already exists");

        Category category = categoryMapper.toEntity(request);
        Integer parentId = request.getParentID();
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
        category.setImage(fileName);
        categoryRepository.save(category);
        fileUploadService.categoryImageUpload(request.getImage(), category.getId());

        return categoryMapper.toCategoryDetailResponse(category, parentId);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDetailResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        Integer parentId = category.getParent() == null ? null : category.getParent().getId();
        var categoryDto = categoryMapper.toCategoryDetailResponse(category, parentId);
        categoryDto.setImage(getCategoryImageURL(category));
        return categoryDto;
    }

    @Override
    public CategoryDetailResponse updateCategory(Integer id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        boolean isNameUnique = !categoryRepository.existsByNameAndIdNot(request.getName(), id);
        if (!isNameUnique)
            throw new RuntimeException("Category already exists");

        category.setName(request.getName());
        category.setAlias(request.getAlias());
        category.setEnabled(request.isEnabled());

        Integer parentId = request.getParentID();
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        // Handle image upload if present
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
            fileUploadService.categoryImageUpload(request.getImage(), category.getId());
            category.setImage(fileName);
        }

        categoryRepository.save(category);
        return categoryMapper.toCategoryDetailResponse(category, parentId);
    }

    @Override
    public void updateCategoryStatus(Integer id, boolean status) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setEnabled(status);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryExportResponse> listAllForExport() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toCategoryExportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryListResponse> listChildren(Integer id) {
        List<Category> children = categoryRepository.findChildren(id);
        return children.stream()
                .map(category -> {
                    boolean hasChildren = categoryRepository.hasChildren(category.getId());
                    var categoryDto = categoryMapper.toCategoryListResponse(category, hasChildren);
                    categoryDto.setImage(getCategoryImageURL(category));
                    return categoryDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CategorySelectResponse> getAllInForm() {
        List<Object[]> rawData = categoryRepository.findCategoryTree();
        Map<Integer, CategorySelectResponse> categoryMap = new HashMap<>();
        List<CategorySelectResponse> rootCategories = new ArrayList<>();

        for (Object[] row : rawData) {
            CategorySelectResponse category = new CategorySelectResponse();
            category.setId((Integer) row[0]);
            category.setName((String) row[1]);
            category.setAlias((String) row[2]);
            category.setChildren(new ArrayList<>());

            categoryMap.put(category.getId(), category);

            Integer parentId = (Integer) row[3];
            if (parentId == null) {
                rootCategories.add(category);
            } else {
                CategorySelectResponse parent = categoryMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(category);
                }
            }
        }

        return rootCategories;
    }
}
