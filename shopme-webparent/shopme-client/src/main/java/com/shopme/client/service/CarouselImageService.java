package com.shopme.client.service;

import com.shopme.client.dto.response.CarouselImageResponse;

import java.util.List;

public interface CarouselImageService {

    List<CarouselImageResponse> getCarouselImagesEnabled();
}
