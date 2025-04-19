package com.shopme.client.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis cache configuration for Shopme e-commerce application
 * Provides optimized caching configuration for different data types
 * with TTL values based on data change frequency and access patterns
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Default cache configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))  // Default TTL: 10 minutes
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // Configure specific TTL for different cache names
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // === TIER 1 (Highest Priority) ===
        
        // Product Price calculations (highest traffic, critical for accuracy)
        cacheConfigurations.put("productPriceCache", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Product listings (high traffic, moderately changing data)
        cacheConfigurations.put("productListings", defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put("bestSellerProducts", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put("trendingProducts", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put("topRatedProducts", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put("discountedProducts", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        
        // Feature Categories and Brands for navigation (high traffic, rarely changing)
        cacheConfigurations.put("featureCategoryBrand", defaultConfig.entryTtl(Duration.ofMinutes(60)));
        
        // === TIER 2 (Medium Priority) ===
        
        // Product details (medium traffic, data changes occasionally)
        cacheConfigurations.put("productDetail", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Categories (high traffic, rarely changing data)
        cacheConfigurations.put("rootCategories", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("categoryChildren", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("categoryDetail", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // Location data (medium traffic, very rarely changing)
        cacheConfigurations.put("cityCache", defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurations.put("provinceCache", defaultConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurations.put("districtCache", defaultConfig.entryTtl(Duration.ofHours(24)));
        
        // Carousel images (high on homepage, rarely changing)
        cacheConfigurations.put("carouselImages", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // === TIER 3 (Lower Priority) ===
        
        // Brands for filtering (medium traffic, rarely changing data)
        cacheConfigurations.put("brandCache", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Client settings (medium traffic, rarely changing)
        cacheConfigurations.put("clientSettings", defaultConfig.entryTtl(Duration.ofMinutes(60)));
        
        // Promotion related (medium traffic, moderate change frequency)
        cacheConfigurations.put("promotionTypes", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("promotionDetail", defaultConfig.entryTtl(Duration.ofMinutes(20)));
        cacheConfigurations.put("promotionProducts", defaultConfig.entryTtl(Duration.ofMinutes(10)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
