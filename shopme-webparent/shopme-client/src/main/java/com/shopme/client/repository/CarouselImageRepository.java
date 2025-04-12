package com.shopme.client.repository;

import com.shopme.common.entity.CarouselImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CarouselImageRepository extends JpaRepository<CarouselImage, Integer> {
}
