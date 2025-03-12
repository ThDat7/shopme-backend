package com.shopme.admin.service;

import com.shopme.admin.dto.request.CategoryCreateRequest;
import com.shopme.admin.dto.request.CategoryUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.CategoryMapper;
import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.service.impl.CategoryServiceImpl;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Tests")
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private FileUploadService fileUploadService;

    private CategoryCreateRequest categoryCreateRequest;
    private Category category;
    private Category parentCategory;
    private CategoryDetailResponse categoryDetailResponse;
    private CategoryListResponse categoryListResponse;
    private CategorySelectResponse categorySelectResponse;

    @BeforeEach
    void setUp() {
        // Setup parent category
        parentCategory = new Category();
        parentCategory.setId(1);
        parentCategory.setName("Electronics");
        parentCategory.setAlias("electronics");
        parentCategory.setImage("test.jpg");
        parentCategory.setEnabled(true);

        // Setup child category
        category = new Category();
        category.setId(2);
        category.setName("Computers");
        category.setAlias("computers");
        category.setImage("test.jpg");
        category.setEnabled(true);
        category.setParent(parentCategory);

        // Setup create request with image
        categoryCreateRequest = new CategoryCreateRequest();
        categoryCreateRequest.setName("Computers");
        categoryCreateRequest.setAlias("computers");
        categoryCreateRequest.setEnabled(true);
        categoryCreateRequest.setParentID(1);
        categoryCreateRequest.setImage(new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()));

        // Setup responses
        categoryDetailResponse = new CategoryDetailResponse();
        categoryDetailResponse.setId(2);
        categoryDetailResponse.setName("Computers");
        categoryDetailResponse.setAlias("computers");
        categoryDetailResponse.setEnabled(true);
        categoryDetailResponse.setParentID(1);

        categoryListResponse = new CategoryListResponse();
        categoryListResponse.setId(2);
        categoryListResponse.setName("Computers");
        categoryListResponse.setAlias("computers");
        categoryListResponse.setEnabled(true);
        categoryListResponse.setHasChildren(false);

        categorySelectResponse = new CategorySelectResponse();
        categorySelectResponse.setId(2);
        categorySelectResponse.setName("Computers");
        categorySelectResponse.setAlias("computers");
        categorySelectResponse.setChildren(new ArrayList<>());
    }

    @Nested
    @DisplayName("Create Category Operations")
    class CreateCategoryTests {
        @Test
        @DisplayName("Should create category successfully")
        void createCategory_Success() {
            // Arrange
            when(categoryRepository.existsByName(anyString())).thenReturn(false);
            when(categoryMapper.toEntity(any(CategoryCreateRequest.class))).thenReturn(category);
            when(categoryRepository.findById(1)).thenReturn(Optional.of(parentCategory));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(categoryMapper.toCategoryDetailResponse(any(Category.class), anyInt())).thenReturn(categoryDetailResponse);

            // Act
            CategoryDetailResponse result = categoryService.createCategory(categoryCreateRequest);

            // Assert
            assertNotNull(result);
            assertEquals("Computers", result.getName());
            verify(categoryRepository).save(category);
            verify(categoryMapper).toCategoryDetailResponse(category, 1);
            verify(fileUploadService).categoryImageUpload(any(), anyInt());
        }

        @Test
        @DisplayName("Should throw exception when image is missing")
        void createCategory_MissingImage_ThrowsException() {
            // Arrange
            categoryCreateRequest.setImage(null);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                categoryService.createCategory(categoryCreateRequest);
            });

            assertEquals("Category image is required", exception.getMessage());
            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw exception when category name exists")
        void createCategory_NameExists_ThrowsException() {
            // Arrange
            when(categoryRepository.existsByName(anyString())).thenReturn(true);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                categoryService.createCategory(categoryCreateRequest);
            });

            assertEquals("Category already exists", exception.getMessage());
            verify(categoryRepository, never()).save(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Get Categories For Form Operations")
    class GetCategoriesForFormTests {
        @Test
        @DisplayName("Should get hierarchical categories successfully")
        void getAllInForm_Success() {
            // Arrange
            List<Object[]> rawData = Arrays.asList(
                    new Object[]{1, "Electronics", "electronics", null},
                    new Object[]{2, "Computers", "computers", 1},
                    new Object[]{3, "Laptops", "laptops", 2});

            when(categoryRepository.findCategoryTree()).thenReturn(rawData);

            // Act
            List<CategorySelectResponse> result = categoryService.getAllInForm();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size()); // One root category
            assertEquals("Electronics", result.get(0).getName());

            List<CategorySelectResponse> firstLevelChildren = result.get(0).getChildren();
            assertEquals(1, firstLevelChildren.size());
            assertEquals("Computers", firstLevelChildren.get(0).getName());

            List<CategorySelectResponse> secondLevelChildren = firstLevelChildren.get(0).getChildren();
            assertEquals(1, secondLevelChildren.size());
            assertEquals("Laptops", secondLevelChildren.get(0).getName());
        }

        @Test
        @DisplayName("Should handle empty category list")
        void getAllInForm_EmptyList() {
            // Arrange
            when(categoryRepository.findCategoryTree()).thenReturn(new ArrayList<>());

            // Act
            List<CategorySelectResponse> result = categoryService.getAllInForm();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("List Categories Operations")
    class ListCategoriesTests {
        @Test
        @DisplayName("Should list categories by page successfully")
        void listByPage_Success() {
            // Arrange
            List<Category> categories = Arrays.asList(category);
            Page<Category> categoryPage = new PageImpl<>(categories);
            Map<String, String> params = new HashMap<>();
            params.put("page", "0");
            params.put("size", "10");

            when(categoryRepository.findRootCategories(any(Pageable.class))).thenReturn(categoryPage);
            when(categoryRepository.hasChildren(anyInt())).thenReturn(false);
            when(categoryMapper.toCategoryListResponse(any(Category.class), anyBoolean())).thenReturn(categoryListResponse);

            // Act
            ListResponse<CategoryListResponse> result = categoryService.listByPage(params);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalPages());
            assertEquals("Computers", result.getContent().get(0).getName());
            verify(categoryRepository).findRootCategories(any(Pageable.class));
            verify(categoryMapper).toCategoryListResponse(category, false);
        }
    }

    @Nested
    @DisplayName("Search Categories Operations")
    class SearchCategoriesTests {
        @Test
        @DisplayName("Should search categories successfully")
        void search_Success() {
            // Arrange
            List<Category> categories = Arrays.asList(category);
            Page<Category> categoryPage = new PageImpl<>(categories);
            Map<String, String> params = new HashMap<>();
            params.put("keyword", "Computer");

            when(categoryRepository.findAll(anyString(), any(Pageable.class))).thenReturn(categoryPage);
            when(categoryRepository.findBreadcrumbById(anyInt())).thenReturn("Electronics > Computers");
            when(categoryMapper.toCategorySearchResponse(any(Category.class), anyString()))
                    .thenReturn(new CategorySearchResponse());

            // Act
            ListResponse<CategorySearchResponse> result = categoryService.search(params);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalPages());
            verify(categoryRepository).findAll(anyString(), any(Pageable.class));
            verify(categoryMapper).toCategorySearchResponse(any(), anyString());
        }

        @Test
        @DisplayName("Should throw exception when keyword is null")
        void search_NullKeyword_ThrowsException() {
            // Arrange
            Map<String, String> params = new HashMap<>();

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                categoryService.search(params);
            });

            assertEquals("Keyword cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Delete Category Operations")
    class DeleteCategoryTests {
        @Test
        @DisplayName("Should delete category successfully")
        void deleteCategory_Success() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            doNothing().when(categoryRepository).delete(any(Category.class));

            // Act
            categoryService.deleteCategory(1);

            // Assert
            verify(categoryRepository).findById(1);
            verify(categoryRepository).delete(category);
        }

        @Test
        @DisplayName("Should throw exception when category not found")
        void deleteCategory_NotFound_ThrowsException() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                categoryService.deleteCategory(999);
            });

            assertEquals("Category not found", exception.getMessage());
            verify(categoryRepository, never()).delete(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Get Category Operations")
    class GetCategoryTests {
        @Test
        @DisplayName("Should get category by id successfully")
        void getCategoryById_Success() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            when(categoryMapper.toCategoryDetailResponse(any(Category.class), anyInt())).thenReturn(categoryDetailResponse);
            when(fileUploadService.getCategoryImageUrl(anyInt(), anyString())).thenReturn("image-url");

            // Act
            CategoryDetailResponse result = categoryService.getCategoryById(1);

            // Assert
            assertNotNull(result);
            assertEquals("Computers", result.getName());
            verify(categoryRepository).findById(1);
            verify(categoryMapper).toCategoryDetailResponse(category, 1);
            verify(fileUploadService).getCategoryImageUrl(anyInt(), anyString());
        }

        @Test
        @DisplayName("Should throw exception when category not found")
        void getCategoryById_NotFound_ThrowsException() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                categoryService.getCategoryById(999);
            });

            assertEquals("Category not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Update Category Operations")
    class UpdateCategoryTests {
        private CategoryUpdateRequest updateRequest;

        @BeforeEach
        void setUp() {
            updateRequest = new CategoryUpdateRequest();
            updateRequest.setName("Updated Computers");
            updateRequest.setAlias("updated-computers");
            updateRequest.setEnabled(true);
            updateRequest.setParentID(1);
        }

        @Test
        @DisplayName("Should update category successfully")
        void updateCategory_Success() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            when(categoryRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(parentCategory));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(categoryMapper.toCategoryDetailResponse(any(Category.class), anyInt())).thenReturn(categoryDetailResponse);

            // Act
            CategoryDetailResponse result = categoryService.updateCategory(1, updateRequest);

            // Assert
            assertNotNull(result);

            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
            verify(categoryRepository).save(categoryCaptor.capture());
            assertEquals("updated-computers", categoryCaptor.getValue().getAlias());

            verify(categoryMapper).toCategoryDetailResponse(any(Category.class), anyInt());
        }

        @Test
        @DisplayName("Should update category with image successfully")
        void updateCategory_WithImage_Success() {
            // Arrange
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes());
            updateRequest.setImage(file);

            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            when(categoryRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(parentCategory));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(categoryMapper.toCategoryDetailResponse(any(Category.class), anyInt())).thenReturn(categoryDetailResponse);

            // Act
            CategoryDetailResponse result = categoryService.updateCategory(1, updateRequest);

            // Assert
            assertNotNull(result);
            verify(fileUploadService).categoryImageUpload(any(), anyInt());

            ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
            verify(categoryRepository).save(categoryCaptor.capture());
            assertEquals("updated-computers", categoryCaptor.getValue().getAlias());
            assertEquals("test.jpg", categoryCaptor.getValue().getImage());

            verify(categoryMapper).toCategoryDetailResponse(any(Category.class), anyInt());
        }

        @Test
        @DisplayName("Should throw exception when category name exists")
        void updateCategory_NameExists_ThrowsException() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            when(categoryRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(true);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                categoryService.updateCategory(1, updateRequest);
            });

            assertEquals("Category already exists", exception.getMessage());
            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw exception when category not found")
        void updateCategory_NotFound_ThrowsException() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                categoryService.updateCategory(999, updateRequest);
            });

            assertEquals("Category not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Update Category Status Operations")
    class UpdateCategoryStatusTests {
        @Test
        @DisplayName("Should update category status successfully")
        void updateCategoryStatus_Success() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            // Act
            categoryService.updateCategoryStatus(1, true);

            // Assert
            verify(categoryRepository).findById(1);
            verify(categoryRepository).save(category);
            assertTrue(category.isEnabled());
        }

        @Test
        @DisplayName("Should throw exception when category not found")
        void updateCategoryStatus_NotFound_ThrowsException() {
            // Arrange
            when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                categoryService.updateCategoryStatus(999, true);
            });

            assertEquals("Category not found", exception.getMessage());
            verify(categoryRepository, never()).save(any(Category.class));
        }
    }

    @Nested
    @DisplayName("List Children Operations")
    class ListChildrenTests {
        @Test
        @DisplayName("Should list children successfully")
        void listChildren_Success() {
            // Arrange
            List<Category> children = Arrays.asList(category);
            when(categoryRepository.findChildren(anyInt())).thenReturn(children);
            when(categoryRepository.hasChildren(anyInt())).thenReturn(false);
            when(categoryMapper.toCategoryListResponse(any(Category.class), anyBoolean())).thenReturn(categoryListResponse);
            when(fileUploadService.getCategoryImageUrl(anyInt(), anyString())).thenReturn("image-url");

            // Act
            List<CategoryListResponse> result = categoryService.listChildren(1);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Computers", result.get(0).getName());
            verify(categoryRepository).findChildren(1);
            verify(categoryMapper).toCategoryListResponse(category, false);
            verify(fileUploadService).getCategoryImageUrl(anyInt(), anyString());
        }
    }

    @Nested
    @DisplayName("Export Categories Operations")
    class ExportCategoriesTests {
        @Test
        @DisplayName("Should export all categories successfully")
        void listAllForExport_Success() {
            // Arrange
            List<Category> categories = Arrays.asList(category);
            CategoryExportResponse exportResponse = new CategoryExportResponse();
            exportResponse.setId(2);
            exportResponse.setName("Computers");

            when(categoryRepository.findAll()).thenReturn(categories);
            when(categoryMapper.toCategoryExportResponse(any(Category.class))).thenReturn(exportResponse);

            // Act
            List<CategoryExportResponse> result = categoryService.listAllForExport();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Computers", result.get(0).getName());
            verify(categoryRepository).findAll();
            verify(categoryMapper).toCategoryExportResponse(category);
        }
    }
}