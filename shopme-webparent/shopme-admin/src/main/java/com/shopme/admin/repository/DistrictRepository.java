package com.shopme.admin.repository;

import com.shopme.common.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
}
