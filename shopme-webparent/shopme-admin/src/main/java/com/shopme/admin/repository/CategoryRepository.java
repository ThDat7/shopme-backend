package com.shopme.admin.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    Page<Category> findRootCategories(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword% OR c.alias LIKE %:keyword%")
    Page<Category> findAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.parent.id = :id")
    Boolean hasChildren(@Param("id") Integer id);

    @Query(value = """
            WITH RECURSIVE category_path AS (
                        SELECT name AS path, parent_id
                        FROM categories
                        WHERE id = :id
                        UNION ALL
                        SELECT CONCAT(cp.path, ' > ', c.name), c.parent_id
                        FROM categories c
                        INNER JOIN category_path cp ON c.id = cp.parent_id
                    )
                    SELECT path FROM category_path ORDER BY LENGTH(path) DESC LIMIT 1
            """, nativeQuery = true)
    String findBreadcrumbById(@Param("id") Integer id);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :id")
    List<Category> findChildren(@Param("id") Integer id);

}
