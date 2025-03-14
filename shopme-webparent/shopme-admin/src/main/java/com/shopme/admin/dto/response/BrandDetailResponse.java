package com.shopme.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDetailResponse {
    private Integer id;
    private String name;
    private String logo;
    private Set<Integer> categoryIds;
}
