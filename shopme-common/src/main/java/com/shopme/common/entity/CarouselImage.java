package com.shopme.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "carousel_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarouselImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128)
    private String image;

    @Column(length = 128)
    private String name;

    @Column(length = 128)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean enabled;
}
