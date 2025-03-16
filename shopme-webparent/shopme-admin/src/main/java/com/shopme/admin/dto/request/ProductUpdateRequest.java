package com.shopme.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest extends BaseProductRequest {
    private MultipartFile mainImage;
    private Set<MultipartFile> images;
    private Set<Integer> remainingImageIds;
}
