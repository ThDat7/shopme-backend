package com.shopme.client.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c LEFT JOIN c.children children WHERE children IS NULL")
    List<Category> findLeafCategories();

    @Query(value = """
    WITH RECURSIVE category_tree AS (
        SELECT id FROM categories WHERE parent_id = :parentId
        UNION ALL
        SELECT c.id FROM categories c
        JOIN category_tree ct ON c.parent_id = ct.id
    )
    SELECT id FROM category_tree
    """, nativeQuery = true)
    List<Integer> findAllChildCategoryIds(@Param("parentId") Integer parentId);

    // Sau đó dùng:
    @Query("""
    SELECT 
        c.id, c.name, c.image, c.parent.id,
        (SELECT COUNT(DISTINCT p.id) FROM Product p WHERE p.category.id IN 
            (SELECT c2.id FROM Category c2 WHERE c2.id = c.id OR c2.parent.id = c.id OR 
             (c2.allParentIDs IS NOT NULL AND FUNCTION('FIND_IN_SET', CAST(c.id AS string), c2.allParentIDs) > 0))),
        CASE WHEN SIZE(c.children) > 0 THEN true ELSE false END
    FROM Category c
    WHERE c.id IN :categoryIds
    GROUP BY c.id, c.name, c.image, c.parent.id
    """)
    List<Object[]> findCategoriesWithProductCount(@Param("categoryIds") List<Integer> categoryIds);


    @Query("""
    SELECT 
        c.id, c.name, c.image, c.parent.id,
        (SELECT COUNT(DISTINCT p.id) FROM Product p WHERE p.category.id IN 
            (SELECT c2.id FROM Category c2 WHERE c2.id = c.id OR c2.parent.id = c.id OR 
             (c2.allParentIDs IS NOT NULL AND FUNCTION('FIND_IN_SET', CAST(c.id AS string), c2.allParentIDs) > 0))),
        CASE WHEN SIZE(c.children) > 0 THEN true ELSE false END
    FROM Category c
    WHERE c.id = :id
    GROUP BY c.id, c.name, c.image, c.parent.id
    """)
    Optional<Object[]> findCategoriesWithProductCount(@Param("id") Integer id);

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

    @Query(value = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM categories WHERE parent_id IS NULL
                UNION ALL
                SELECT c.id FROM categories c
                JOIN category_tree ct ON c.parent_id = ct.id
            ),
            root_categories AS (
                SELECT c.id, c.name, c.image, c.parent_id 
                FROM categories c
                WHERE c.parent_id IS NULL
            )
            SELECT 
                rc.id, 
                rc.name, 
                rc.image, 
                rc.parent_id,
                (SELECT COUNT(p.id) FROM products p 
                 WHERE p.category_id IN (
                    SELECT c.id FROM categories c 
                    WHERE c.id = rc.id OR c.parent_id = rc.id OR 
                    (c.all_parent_ids IS NOT NULL AND FIND_IN_SET(rc.id, c.all_parent_ids) > 0)
                 )),
                CASE WHEN EXISTS (SELECT 1 FROM categories c WHERE c.parent_id = rc.id) THEN 1 ELSE 0 END
            FROM root_categories rc
            ORDER BY rc.name ASC
            LIMIT :pageSize OFFSET :offset
            """, nativeQuery = true)
    List<Object[]> findRootCategories(@Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = """
            SELECT COUNT(*) FROM categories WHERE parent_id IS NULL
            """, nativeQuery = true)
    long countRootCategories();
}
