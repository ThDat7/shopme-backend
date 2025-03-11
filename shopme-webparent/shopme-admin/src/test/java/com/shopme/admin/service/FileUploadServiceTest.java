package com.shopme.admin.service;

import com.shopme.admin.service.impl.LocalFileUploadServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private Path uploadPath;

    @BeforeEach
    void setUp() throws IOException {
        suffixUserPhotos = "test-uploads";
        var tempPath = Paths.get(suffixUserPhotos);
        uploadPath = Files.createDirectories(tempPath);

        // Manually inject the properties using ReflectionTestUtils
        ReflectionTestUtils.setField(fileUploadService, "suffixUserPhotos", suffixUserPhotos);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up temporary directories
        Files.walk(uploadPath)
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
        // Assert that directories exist after init
        assertTrue(Files.exists(uploadPath));
    }

    @Test
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