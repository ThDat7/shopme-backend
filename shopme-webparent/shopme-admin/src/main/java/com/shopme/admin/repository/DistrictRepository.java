package com.shopme.admin.repository;

import com.shopme.common.entity.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query("SELECT s FROM District s " +
            "WHERE (:keyword IS NULL OR s.name LIKE %:keyword%)" +
            "AND (:provinceId IS NULL OR s.province.id = :provinceId)")
    Page<District> findAll(@Param("keyword") String keyword, @Param("provinceId") Integer provinceId, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    List<District> findAllByProvinceId(Integer provinceId);
}
