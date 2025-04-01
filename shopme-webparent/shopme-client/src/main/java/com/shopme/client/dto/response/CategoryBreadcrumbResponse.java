package com.shopme.client.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBreadcrumbResponse {
    private Integer id;
    private String name;
    private String alias;
}
