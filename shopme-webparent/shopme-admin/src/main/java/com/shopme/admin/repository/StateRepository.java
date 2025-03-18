package com.shopme.admin.repository;

import com.shopme.common.entity.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    @Query("SELECT s FROM State s " +
            "WHERE (:keyword IS NULL OR s.name LIKE %:keyword%)" +
            "AND (:countryId IS NULL OR s.country.id = :countryId)")
    Page<State> findAll(@Param("keyword") String keyword, @Param("countryId") Integer countryId, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
