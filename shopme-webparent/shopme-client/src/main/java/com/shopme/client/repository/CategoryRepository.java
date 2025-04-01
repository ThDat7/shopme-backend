package com.shopme.client.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c LEFT JOIN c.children children WHERE children IS NULL")
    List<Category> findLeafCategories();

    List<Category> findByParentId(Integer parentId);

    @Query(value = """
            WITH RECURSIVE category_parent AS (
                        SELECT c.id, c.name, c.alias, c.parent_id
                        FROM categories c
                        WHERE id = :id
                        UNION ALL
                        SELECT c.id, c.name, c.alias, c.parent_id
                        FROM categories c
                        INNER JOIN category_parent cp ON c.id = cp.parent_id
                    )
                    SELECT id, name, alias FROM category_parent
            """, nativeQuery = true)
    List<Object[]> findParentCategories(@Param("id") Integer id);
}
