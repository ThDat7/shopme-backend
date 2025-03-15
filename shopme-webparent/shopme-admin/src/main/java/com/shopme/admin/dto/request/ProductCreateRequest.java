package com.shopme.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest extends BaseProductRequest {
    //    jakarta validation will be added here
    private MultipartFile mainImage;

    private Set<MultipartFile> images;
}
