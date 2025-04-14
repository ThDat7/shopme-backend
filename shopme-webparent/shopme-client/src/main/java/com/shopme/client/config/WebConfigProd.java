package com.shopme.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc configuration for production/Docker environment
 * Uses absolute paths to resolve resource locations
 */
@Configuration
@Profile("prod")
public class WebConfigProd implements WebMvcConfigurer {

    private final String FRONTEND_HOST;

    public WebConfigProd(@Value("${frontend.host}") String frontendHost) {
        this.FRONTEND_HOST = frontendHost;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Using absolute paths for Docker environment
        exposeDirectory("/app/category-images", registry, "category-images");
        exposeDirectory("/app/brand-logos", registry, "brand-logos");
        exposeDirectory("/app/product-images", registry, "product-images");
        exposeDirectory("/app/carousel-images", registry, "carousel-images");
    }

    /**
     * Expose a directory with a specific logical path
     * 
     * @param absolutePath The absolute path to the directory
     * @param registry The resource handler registry
     * @param logicalPathName The logical path name for resource access
     */
    private void exposeDirectory(String absolutePath, ResourceHandlerRegistry registry, String logicalPathName) {
        registry.addResourceHandler(logicalPathName + "/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}
