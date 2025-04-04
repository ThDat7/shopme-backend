package com.shopme.client.repository;

import com.shopme.common.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findAllByDistrictId(Integer districtId);
}
