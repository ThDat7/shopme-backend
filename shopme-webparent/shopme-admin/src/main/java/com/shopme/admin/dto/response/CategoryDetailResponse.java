package com.shopme.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailResponse {
    private Integer id;
    private String name;
    private String alias;
    private String image;
    private boolean enabled;
    private Integer parentID;
}
