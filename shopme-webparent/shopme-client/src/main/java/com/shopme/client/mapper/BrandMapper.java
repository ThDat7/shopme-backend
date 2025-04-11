package com.shopme.client.mapper;

import com.shopme.client.dto.response.BrandResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResponse toBrandResponse(Integer id, String name, String image, Long productCount);
}
