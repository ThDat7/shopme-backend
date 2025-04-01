package com.shopme.client.repository;

import com.shopme.common.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findAllByProvinceId(Integer provinceId);
}
