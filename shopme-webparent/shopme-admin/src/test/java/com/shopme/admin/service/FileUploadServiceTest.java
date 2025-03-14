package com.shopme.admin.service;

import com.shopme.admin.service.impl.LocalFileUploadServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileUploadServiceTest {
    @InjectMocks
    private LocalFileUploadServiceImpl fileUploadService;

    private String suffixUserPhotos;
    private String suffixCategoryImages;
    private String suffixBrandLogos;
    private Path userUploadPath;
    private Path categoryUploadPath;
    private Path brandLogoPath;

    @BeforeEach
    void setUp() throws IOException {
        String suffixTestFolder = "test-uploads";
        suffixUserPhotos = String.format("%s/user-photos", suffixTestFolder);
        suffixCategoryImages = String.format("%s/category-images", suffixTestFolder);
        suffixBrandLogos = String.format("%s/brand-logos", suffixTestFolder);

        var userTempPath = Paths.get(suffixUserPhotos);
        userUploadPath = Files.createDirectories(userTempPath);

        var categoryTempPath = Paths.get(suffixCategoryImages);
        categoryUploadPath = Files.createDirectories(categoryTempPath);

        var brandLogoTempPath = Paths.get(suffixBrandLogos);
        brandLogoPath = Files.createDirectories(brandLogoTempPath);

        // Manually inject the properties using ReflectionTestUtils
        ReflectionTestUtils.setField(fileUploadService, "suffixUserPhotos", suffixUserPhotos);
        ReflectionTestUtils.setField(fileUploadService, "suffixCategoryImages", suffixCategoryImages);
        ReflectionTestUtils.setField(fileUploadService, "suffixBrandLogos", suffixBrandLogos);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up temporary directories
        Files.walk(userUploadPath)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        System.err.println("Failed to delete: " + path);
                    }
                });

        Files.walk(categoryUploadPath)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        System.err.println("Failed to delete: " + path);
                    }
                });

        Files.walk(brandLogoPath)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        System.err.println("Failed to delete: " + path);
                    }
                });
    }

    @Test
    void init_CreatesDirectories() {
        assertTrue(Files.exists(userUploadPath));
        assertTrue(Files.exists(categoryUploadPath));
        assertTrue(Files.exists(brandLogoPath));
    }

    @Nested
    @DisplayName("User Photos Upload Operations")
    class UserPhotosUploadTests {
        @Test
        @DisplayName("Should upload user photo successfully")
        void userPhotosUpload_Success() {
            // Arrange
            Integer userId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test-image.jpg",
                    "image/jpeg",
                    "test image content".getBytes());

            // Act
            String savedFileName = fileUploadService.userPhotosUpload(file, userId);

            // Assert
            assertNotNull(savedFileName);
            assertTrue(savedFileName.contains("test-image.jpg"));
            assertTrue(Files.exists(Paths.get(String.format("%s/%s", suffixUserPhotos, userId), savedFileName)));
        }

        @Test
        @DisplayName("Should throw exception when IO error occurs")
        void userPhotosUpload_WithIOException_ThrowsRuntimeException() {
            // Arrange
            Integer userId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test-image.jpg",
                    "image/jpeg",
                    "test image content".getBytes()) {
                @Override
                public InputStream getInputStream() throws IOException {
                    throw new IOException("Test exception");
                }
            };

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                fileUploadService.userPhotosUpload(file, userId);
            });

            assertTrue(exception.getMessage().contains("Could not save file"));
        }

        @Test
        @DisplayName("Should throw exception when file is null")
        void userPhotosUpload_WithNullFile_ThrowsIllegalArgumentException() {
            // Arrange
            Integer userId = 1;
            MultipartFile file = null;

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                fileUploadService.userPhotosUpload(file, userId);
            });

            assertTrue(exception.getMessage().contains("File cannot be null"));
        }

        @Test
        @DisplayName("Should throw exception when file is empty")
        void userPhotosUpload_WithEmptyFile_ThrowsIllegalArgumentException() {
            // Arrange
            Integer userId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "",
                    "image/jpeg",
                    new byte[0]);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                fileUploadService.userPhotosUpload(file, userId);
            });

            assertTrue(exception.getMessage().contains("File cannot be empty"));
        }
    }

    @Nested
    @DisplayName("Category image Upload Operations")
    class CategoryImageUploadTests {
        @Test
        @DisplayName("Should upload category image successfully")
        void categoryImageUpload_Success() {
            // Arrange
            Integer categoryId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test-category.jpg",
                    "image/jpeg",
                    "test image content".getBytes());

            // Act
            String savedFileName = fileUploadService.categoryImageUpload(file, categoryId);

            // Assert
            assertNotNull(savedFileName);
            assertTrue(savedFileName.contains("test-category.jpg"));
            assertTrue(Files
                    .exists(Paths.get(String.format("%s/%s", suffixCategoryImages, categoryId), savedFileName)));
        }

        @Test
        @DisplayName("Should throw exception when IO error occurs")
        void categoryImageUpload_WithIOException_ThrowsRuntimeException() {
            // Arrange
            Integer categoryId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test-category.jpg",
                    "image/jpeg",
                    "test image content".getBytes()) {
                @Override
                public InputStream getInputStream() throws IOException {
                    throw new IOException("Test exception");
                }
            };

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                fileUploadService.categoryImageUpload(file, categoryId);
            });

            assertTrue(exception.getMessage().contains("Could not save file"));
        }

        @Test
        @DisplayName("Should throw exception when file is null")
        void categoryImageUpload_WithNullFile_ThrowsIllegalArgumentException() {
            // Arrange
            Integer categoryId = 1;
            MultipartFile file = null;

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                fileUploadService.categoryImageUpload(file, categoryId);
            });

            assertTrue(exception.getMessage().contains("File cannot be null"));
        }

        @Test
        @DisplayName("Should throw exception when file is empty")
        void categoryImageUpload_WithEmptyFile_ThrowsIllegalArgumentException() {
            // Arrange
            Integer categoryId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "",
                    "image/jpeg",
                    new byte[0]);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                fileUploadService.categoryImageUpload(file, categoryId);
            });

            assertTrue(exception.getMessage().contains("File cannot be empty"));
        }
    }

    @Nested
    @DisplayName("Brand Logo Upload Operations")
    class BrandLogoUploadTests {
        @Test
        @DisplayName("Should upload brand logo successfully")
        void brandLogoUpload_Success() {
            // Arrange
            Integer brandId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test-brand.jpg",
                    "image/jpeg",
                    "test image content".getBytes());

            // Act
            String savedFileName = fileUploadService.brandLogoUpload(file, brandId);

            // Assert
            assertNotNull(savedFileName);
            assertTrue(savedFileName.contains("test-brand.jpg"));
            assertTrue(Files.exists(Paths.get(String.format("%s/%s", suffixBrandLogos, brandId), savedFileName)));
        }

        @Test
        @DisplayName("Should throw exception when IO error occurs")
        void brandLogoUpload_WithIOException_ThrowsRuntimeException() {
            // Arrange
            Integer brandId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "test-brand.jpg",
                    "image/jpeg",
                    "test image content".getBytes()) {
                @Override
                public InputStream getInputStream() throws IOException {
                    throw new IOException("Test exception");
                }
            };

            // Act & Assert
            Exception exception = assertThrows(RuntimeException.class, () -> {
                fileUploadService.brandLogoUpload(file, brandId);
            });

            assertTrue(exception.getMessage().contains("Could not save file"));
        }

        @Test
        @DisplayName("Should throw exception when file is null")
        void brandLogoUpload_WithNullFile_ThrowsIllegalArgumentException() {
            // Arrange
            Integer brandId = 1;
            MultipartFile file = null;

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                fileUploadService.brandLogoUpload(file, brandId);
            });

            assertTrue(exception.getMessage().contains("File cannot be null"));
        }

        @Test
        @DisplayName("Should throw exception when file is empty")
        void brandLogoUpload_WithEmptyFile_ThrowsIllegalArgumentException() {
            // Arrange
            Integer brandId = 1;
            MockMultipartFile file = new MockMultipartFile(
                    "image",
                    "",
                    "image/jpeg",
                    new byte[0]);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                fileUploadService.brandLogoUpload(file, brandId);
            });

            assertTrue(exception.getMessage().contains("File cannot be empty"));
        }
    }
}