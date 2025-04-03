package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"promotion_id", "product_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "discount_percent")
    private Integer discountPercent;
    
    @Column(name = "stock_limit")
    private Integer stockLimit;
}
