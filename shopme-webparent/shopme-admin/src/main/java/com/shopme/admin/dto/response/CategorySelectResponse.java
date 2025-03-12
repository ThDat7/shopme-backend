package com.shopme.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySelectResponse {
    private Integer id;
    private String name;
    private String alias;
    private List<CategorySelectResponse> children;
}
