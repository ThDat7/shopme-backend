package com.shopme.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {
    private Integer id;
    private String name;
    private String alias;
    private boolean enabled;
    private Integer parentID;
    private MultipartFile image;
}
