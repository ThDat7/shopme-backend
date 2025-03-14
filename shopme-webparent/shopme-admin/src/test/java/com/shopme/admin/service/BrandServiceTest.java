package com.shopme.admin.service;

import com.shopme.admin.dto.request.BrandCreateRequest;
import com.shopme.admin.dto.request.BrandUpdateRequest;
import com.shopme.admin.dto.response.BrandDetailResponse;
import com.shopme.admin.dto.response.BrandExportResponse;
import com.shopme.admin.dto.response.BrandListResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.mapper.BrandMapper;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.service.impl.BrandServiceImpl;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@DisplayName("Brand Service Tests")
public class BrandServiceTest {
    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandMapper brandMapper;

    @Mock
    private FileUploadService fileUploadService;

    private BrandCreateRequest brandCreateRequest;
    private Brand brand;
    private Category category;
    private BrandDetailResponse brandDetailResponse;
    private BrandListResponse brandListResponse;
    private BrandExportResponse brandExportResponse;

    @BeforeEach
    void setUp() {
        // Setup category
        category = new Category();
        category.setId(1);
        category.setName("Electronics");
        category.setAlias("electronics");

        // Setup brand
        brand = new Brand();
        brand.setId(1);
        brand.setName("Apple");
        brand.setCategories(Set.of(category));
        brand.setLogo("apple-logo.jpg");

        // Setup create request
        brandCreateRequest = new BrandCreateRequest();
        brandCreateRequest.setName("Apple");
        brandCreateRequest.setCategoryIds(Set.of(1));
        brandCreateRequest.setImage(new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()));

        // Setup responses
        brandDetailResponse = new BrandDetailResponse();
        brandDetailResponse.setId(1);
        brandDetailResponse.setName("Apple");
        brandDetailResponse.setCategoryIds(Set.of(1));

        brandListResponse = new BrandListResponse();
        brandListResponse.setId(1);
        brandListResponse.setName("Apple");

        brandExportResponse = new BrandExportResponse();
        brandExportResponse.setId(1);
        brandExportResponse.setName("Apple");
    }

    @Nested
    @DisplayName("Create Brand Operations")
    class CreateBrandTests {
        @Test
        @DisplayName("Should create brand with logo successfully")
        void createBrand_WithLogo_Success() {
            // Arrange
            when(brandRepository.existsByName(anyString())).thenReturn(false);
            when(brandMapper.toEntity(any(BrandCreateRequest.class))).thenReturn(brand);
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
            when(brandRepository.save(any(Brand.class))).thenReturn(brand);
            when(brandMapper.toBrandDetailResponse(any(Brand.class))).thenReturn(brandDetailResponse);

            // Act
            BrandDetailResponse result = brandService.createBrand(brandCreateRequest);

            // Assert
            assertNotNull(result);
            assertEquals("Apple", result.getName());
            verify(fileUploadService).brandLogoUpload(any(), anyInt());
            verify(brandRepository).save(brand);
            verify(brandMapper).toBrandDetailResponse(brand);
        }

        @Test
        @DisplayName("Should throw exception when brand name exists")
        void createBrand_NameExists_ThrowsException() {
            // Arrange
            when(brandRepository.existsByName(anyString())).thenReturn(true);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                brandService.createBrand(brandCreateRequest);
            });

            assertEquals("Brand already exists", exception.getMessage());
            verify(brandRepository, never()).save(any(Brand.class));
        }

        @Test
        @DisplayName("Should throw exception when logo is missing")
        void createBrand_NoLogo_ThrowsException() {
            // Arrange
            brandCreateRequest.setImage(null);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                brandService.createBrand(brandCreateRequest);
            });

            assertEquals("Brand logo is required", exception.getMessage());
            verify(brandRepository, never()).save(any(Brand.class));
            verify(brandMapper, never()).toBrandDetailResponse(any(Brand.class));
            verify(fileUploadService, never()).brandLogoUpload(any(), anyInt());
        }
    }

    @Nested
    @DisplayName("Update Brand Operations")
    class UpdateBrandTests {
        private BrandUpdateRequest updateRequest;

        @BeforeEach
        void setUp() {
            updateRequest = new BrandUpdateRequest();
            updateRequest.setName("Updated Apple");
            updateRequest.setCategoryIds(Set.of(1));
        }

        @Test
        @DisplayName("Should update brand successfully")
        void updateBrand_Success() {
            // Arrange
            when(brandRepository.findById(anyInt())).thenReturn(Optional.of(brand));
            when(brandRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
            when(brandRepository.save(any(Brand.class))).thenReturn(brand);
            when(brandMapper.toBrandDetailResponse(any(Brand.class))).thenReturn(brandDetailResponse);

            // Act
            BrandDetailResponse result = brandService.updateBrand(1, updateRequest);

            // Assert
            assertNotNull(result);
            verify(brandRepository).save(brand);
            verify(brandMapper).toBrandDetailResponse(brand);
        }

        @Test
        @DisplayName("Should update brand with new logo successfully")
        void updateBrand_WithNewLogo_Success() {
            // Arrange
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test.jpg",
                    "image/jpeg",
                    "test image content".getBytes());
            updateRequest.setImage(file);

            when(brandRepository.findById(anyInt())).thenReturn(Optional.of(brand));
            when(brandRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
            when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
            when(brandRepository.save(any(Brand.class))).thenReturn(brand);
            when(brandMapper.toBrandDetailResponse(any(Brand.class))).thenReturn(brandDetailResponse);

            // Act
            BrandDetailResponse result = brandService.updateBrand(1, updateRequest);

            // Assert
            assertNotNull(result);
            verify(fileUploadService).brandLogoUpload(any(), anyInt());
            verify(brandRepository).save(brand);
            verify(brandMapper).toBrandDetailResponse(brand);
        }

        @Test
        @DisplayName("Should throw exception when brand name exists")
        void updateBrand_NameExists_ThrowsException() {
            // Arrange
            when(brandRepository.findById(anyInt())).thenReturn(Optional.of(brand));
            when(brandRepository.existsByNameAndIdNot(anyString(), anyInt())).thenReturn(true);

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                brandService.updateBrand(1, updateRequest);
            });

            assertEquals("Brand already exists", exception.getMessage());
            verify(brandRepository, never()).save(any(Brand.class));
        }

        @Test
        @DisplayName("Should throw exception when brand not found")
        void updateBrand_NotFound_ThrowsException() {
            // Arrange
            when(brandRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                brandService.updateBrand(999, updateRequest);
            });

            assertEquals("Brand not found", exception.getMessage());
            verify(brandRepository, never()).save(any(Brand.class));
        }
    }

    @Nested
    @DisplayName("Delete Brand Operations")
    class DeleteBrandTests {
        @Test
        @DisplayName("Should delete brand successfully")
        void deleteBrand_Success() {
            // Arrange
            when(brandRepository.findById(anyInt())).thenReturn(Optional.of(brand));
            doNothing().when(brandRepository).delete(any(Brand.class));

            // Act
            brandService.deleteBrand(1);

            // Assert
            verify(brandRepository).findById(1);
            verify(brandRepository).delete(brand);
        }

        @Test
        @DisplayName("Should throw exception when brand not found")
        void deleteBrand_NotFound_ThrowsException() {
            // Arrange
            when(brandRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                brandService.deleteBrand(999);
            });

            assertEquals("Brand not found", exception.getMessage());
            verify(brandRepository, never()).delete(any(Brand.class));
        }
    }

    @Nested
    @DisplayName("List Brands Operations")
    class ListBrandsTests {
        @Test
        @DisplayName("Should list brands by page successfully")
        void listByPage_Success() {
            // Arrange
            List<Brand> brands = Arrays.asList(brand);
            Page<Brand> brandPage = new PageImpl<>(brands);
            Map<String, String> params = new HashMap<>();
            params.put("page", "0");
            params.put("size", "10");

            when(brandRepository.findAll(anyString(), any(Pageable.class))).thenReturn(brandPage);
            when(brandMapper.toBrandListResponse(any(Brand.class))).thenReturn(brandListResponse);
            when(fileUploadService.getBrandLogoUrl(anyInt(), anyString())).thenReturn("logo-url");

            // Act
            ListResponse<BrandListResponse> result = brandService.listByPage(params);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalPages());
            assertEquals("Apple", result.getContent().get(0).getName());
            verify(brandRepository).findAll(anyString(), any(Pageable.class));
            verify(brandMapper).toBrandListResponse(brand);
            verify(fileUploadService).getBrandLogoUrl(anyInt(), anyString());
        }
    }

    @Nested
    @DisplayName("Get Brand Operations")
    class GetBrandTests {
        @Test
        @DisplayName("Should get brand by id successfully")
        void getBrandById_Success() {
            // Arrange
            when(brandRepository.findById(anyInt())).thenReturn(Optional.of(brand));
            when(brandMapper.toBrandDetailResponse(any(Brand.class))).thenReturn(brandDetailResponse);
            when(fileUploadService.getBrandLogoUrl(anyInt(), anyString())).thenReturn("logo-url");

            // Act
            BrandDetailResponse result = brandService.getBrandById(1);

            // Assert
            assertNotNull(result);
            assertEquals("Apple", result.getName());
            verify(brandRepository).findById(1);
            verify(brandMapper).toBrandDetailResponse(brand);
            verify(fileUploadService).getBrandLogoUrl(anyInt(), anyString());
        }

        @Test
        @DisplayName("Should throw exception when brand not found")
        void getBrandById_NotFound_ThrowsException() {
            // Arrange
            when(brandRepository.findById(anyInt())).thenReturn(Optional.empty());

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                brandService.getBrandById(999);
            });

            assertEquals("Brand not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Export Brands Operations")
    class ExportBrandsTests {
        @Test
        @DisplayName("Should export all brands successfully")
        void listAllForExport_Success() {
            // Arrange
            List<Brand> brands = Arrays.asList(brand);
            when(brandRepository.findAll()).thenReturn(brands);
            when(brandMapper.toBrandExportResponse(any(Brand.class))).thenReturn(brandExportResponse);

            // Act
            List<BrandExportResponse> result = brandService.listAllForExport();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Apple", result.get(0).getName());
            verify(brandRepository).findAll();
            verify(brandMapper).toBrandExportResponse(brand);
        }
    }
}