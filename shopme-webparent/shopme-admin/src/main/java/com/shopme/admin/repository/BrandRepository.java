package com.shopme.admin.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Boolean existsByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1% ")
    Page<Brand> findAll(String keyword, Pageable pageable);

    boolean existsByNameAndIdNot(String name, Integer id);
}
