package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "feature_category_brand")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureCategoryBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "feature_order")
    private Integer order;

    @OneToMany(mappedBy = "parent")
    private Set<FeatureCategoryBrand> subCategories;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FeatureCategoryBrand parent;
}
