package com.shopme.client.service.impl;

import com.shopme.client.dto.response.CarouselImageResponse;
import com.shopme.client.mapper.CarouselImageMapper;
import com.shopme.client.repository.CarouselImageRepository;
import com.shopme.client.service.CarouselImageService;
import com.shopme.client.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarouselImageServiceImpl implements CarouselImageService {
    private final FileUploadService fileUploadService;
    private final CarouselImageRepository carouselImageRepository;
    private final CarouselImageMapper carouselImageMapper;

    private String getImageUrl(Integer id, String image) {
        return fileUploadService.getCarouselImageUrl(id, image);
    }

    @Override
    public List<CarouselImageResponse> getCarouselImagesEnabled() {
        return carouselImageRepository.findAllByEnabledTrue()
                .stream()
                .map(carouselImage -> {
                    CarouselImageResponse carouselImageResponse = carouselImageMapper.toCarouselImageResponse(carouselImage);
                    carouselImageResponse.setImage(getImageUrl(carouselImage.getId(), carouselImage.getImage()));
                    return carouselImageResponse;
                }).toList();
    }
}
