package com.shopme.client.service.impl;

import com.shopme.client.exception.type.FileUploadException;
import com.shopme.client.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class S3FileUploadServiceImpl implements FileUploadService {
    
    /**
     * Enum representing different types of resources for file uploads
     */
    public enum ResourceType {
        USER_PHOTOS("user-photos"),
        CATEGORY_IMAGES("category-images"),
        BRAND_LOGOS("brand-logos"),
        PRODUCT_MAIN_IMAGES("product-images"),
        PRODUCT_EXTRA_IMAGES("extras"),
        SITE_LOGO("site-logo"),
        CAROUSEL_IMAGES("carousel-images");

        private final String directoryName;

        ResourceType(String directoryName) {
            this.directoryName = directoryName;
        }

        public String getDirectoryName() {
            return directoryName;
        }
    }

    private final String s3BaseUrl;
    private final String bucketName;
    private final S3Client s3Client;

    public S3FileUploadServiceImpl(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.s3.bucket}") String bucketName) {
        
        this.bucketName = bucketName;
        
        // Configure S3 client
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        
        // Construct S3 base URL
        this.s3BaseUrl = String.format("https://%s.s3.%s.amazonaws.com", bucketName, region);
    }

    /**
     * Uploads a file to S3 for a specific resource type and ID
     * 
     * @param file the file to upload
     * @param resourceType the type of resource
     * @param resourceId the ID of the resource
     * @param cleanDirectory whether to clean the directory before upload
     * @return the file name
     */
    private String uploadFile(MultipartFile file, ResourceType resourceType, Integer resourceId, boolean cleanDirectory) {
        if (file == null) throw new IllegalArgumentException("File cannot be null");
        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String keyPrefix = getResourcePath(resourceType, resourceId);
        String fileName = file.getOriginalFilename();

        if (cleanDirectory) {
            cleanDir(keyPrefix);
        }
        
        return saveFile(keyPrefix, fileName, file);
    }

    /**
     * Gets the URL for a resource in S3
     * 
     * @param resourceType the type of resource
     * @param resourceId the ID of the resource
     * @param fileName the file name
     * @return the URL
     */
    private String getResourceUrl(ResourceType resourceType, Integer resourceId, String fileName) {
        return String.format("%s/%s/%s/%s", s3BaseUrl, resourceType.getDirectoryName(), resourceId, fileName);
    }

    /**
     * Gets the URL for a special case like product extras
     * 
     * @param parentResourceType the parent resource type
     * @param parentId the parent ID
     * @param childResourceType the child resource type
     * @param fileName the file name
     * @return the URL
     */
    private String getNestedResourceUrl(ResourceType parentResourceType, Integer parentId, 
                                      ResourceType childResourceType, String fileName) {
        return String.format("%s/%s/%s/%s/%s", 
                s3BaseUrl, 
                parentResourceType.getDirectoryName(), 
                parentId, 
                childResourceType.getDirectoryName(),
                fileName);
    }

    /**
     * Gets the path for a resource in S3
     * 
     * @param resourceType the type of resource
     * @param resourceId the ID of the resource
     * @return the path
     */
    private String getResourcePath(ResourceType resourceType, Integer resourceId) {
        return String.format("%s/%s", resourceType.getDirectoryName(), resourceId);
    }

    /**
     * Gets the path for a nested resource in S3
     * 
     * @param parentResourceType the parent resource type
     * @param parentId the parent ID
     * @param childResourceType the child resource type
     * @return the path
     */
    private String getNestedResourcePath(ResourceType parentResourceType, Integer parentId, ResourceType childResourceType) {
        return String.format("%s/%s/%s", 
                parentResourceType.getDirectoryName(), 
                parentId, 
                childResourceType.getDirectoryName());
    }

    private String saveFile(String keyPrefix, String fileName, MultipartFile multipartFile) {
        try {
            // Generate a unique file name to avoid collisions
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            String key = keyPrefix + "/" + uniqueFileName;
            
            // Upload the file to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(multipartFile.getContentType())
                    .build();
            
            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
            
            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileUploadException();
        }
    }

    private void cleanDir(String keyPrefix) {
        try {
            // List objects with the given prefix
            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(keyPrefix + "/")
                    .build();
            
            ListObjectsV2Response response = s3Client.listObjectsV2(listObjectsRequest);
            List<S3Object> objects = response.contents();
            
            // Delete all objects with the prefix
            for (S3Object object : objects) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(object.key())
                        .build();
                
                s3Client.deleteObject(deleteObjectRequest);
            }
        } catch (Exception ex) {
            System.out.println("Could not clean directory: " + keyPrefix);
        }
    }

    // Implementations of the FileUploadService interface methods

    @Override
    public String userPhotosUpload(MultipartFile file, Integer userId) {
        return uploadFile(file, ResourceType.USER_PHOTOS, userId, true);
    }

    @Override
    public String getUserPhotosURL(Integer userId, String photos) {
        return getResourceUrl(ResourceType.USER_PHOTOS, userId, photos);
    }

    @Override
    public String categoryImageUpload(MultipartFile file, Integer categoryId) {
        return uploadFile(file, ResourceType.CATEGORY_IMAGES, categoryId, true);
    }

    @Override
    public String getCategoryImageUrl(Integer categoryId, String image) {
        return getResourceUrl(ResourceType.CATEGORY_IMAGES, categoryId, image);
    }

    @Override
    public String brandLogoUpload(MultipartFile file, Integer brandId) {
        return uploadFile(file, ResourceType.BRAND_LOGOS, brandId, true);
    }

    @Override
    public String getBrandLogoUrl(Integer brandId, String image) {
        return getResourceUrl(ResourceType.BRAND_LOGOS, brandId, image);
    }

    @Override
    public String productMainImageUpload(MultipartFile file, Integer productId) {
        return uploadFile(file, ResourceType.PRODUCT_MAIN_IMAGES, productId, false);
    }

    @Override
    public String getProductMainImageUrl(Integer productId, String image) {
        return getResourceUrl(ResourceType.PRODUCT_MAIN_IMAGES, productId, image);
    }

    @Override
    public String productImagesUpload(MultipartFile file, Integer productId) {
        // For product extra images, we use a nested path
        String keyPrefix = getNestedResourcePath(
                ResourceType.PRODUCT_MAIN_IMAGES, 
                productId, 
                ResourceType.PRODUCT_EXTRA_IMAGES);
        
        if (file == null) throw new IllegalArgumentException("File cannot be null");
        if (file.isEmpty()) throw new IllegalArgumentException("File cannot be empty");

        String fileName = file.getOriginalFilename();
        return saveFile(keyPrefix, fileName, file);
    }

    @Override
    public String getProductImagesUrl(Integer productId, String image) {
        return getNestedResourceUrl(
                ResourceType.PRODUCT_MAIN_IMAGES, 
                productId, 
                ResourceType.PRODUCT_EXTRA_IMAGES, 
                image);
    }

    @Override
    public String getSiteLogoUrl(String fileName) {
        return String.format("%s/%s/%s", s3BaseUrl, ResourceType.SITE_LOGO.getDirectoryName(), fileName);
    }

    @Override
    public String getCarouselImageUrl(Integer carouselId, String image) {
        return getResourceUrl(ResourceType.CAROUSEL_IMAGES, carouselId, image);
    }
}
