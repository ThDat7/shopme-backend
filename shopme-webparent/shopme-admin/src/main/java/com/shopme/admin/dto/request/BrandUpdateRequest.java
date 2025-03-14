package com.shopme.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandUpdateRequest {
    private String name;
    private MultipartFile image;
    private Set<Integer> categoryIds;
}
