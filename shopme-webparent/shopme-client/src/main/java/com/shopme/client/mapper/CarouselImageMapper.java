package com.shopme.client.mapper;

import com.shopme.client.dto.response.CarouselImageResponse;
import com.shopme.common.entity.CarouselImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarouselImageMapper {
    CarouselImageResponse toCarouselImageResponse(CarouselImage carouselImage);
}
