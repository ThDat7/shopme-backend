package com.shopme.client.controller;


import com.shopme.client.dto.response.CarouselImageResponse;
import com.shopme.client.service.CarouselImageService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carousel-images")
@RequiredArgsConstructor
public class CarouselImageController {
    private final CarouselImageService carouselImageService;

    @GetMapping
    public ApiResponse<List<CarouselImageResponse>> getCarouselImagesEnabled() {
        return ApiResponse.ok(carouselImageService.getCarouselImagesEnabled());
    }
}
