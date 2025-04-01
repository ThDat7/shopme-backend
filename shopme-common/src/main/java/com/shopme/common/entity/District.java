package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "districts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 3)
    private String code;

    @Column(nullable = false, length = 45)
    private String name;

    @ManyToOne
    private Province province;

    @OneToMany(mappedBy = "district")
    private List<Ward> wards;
}