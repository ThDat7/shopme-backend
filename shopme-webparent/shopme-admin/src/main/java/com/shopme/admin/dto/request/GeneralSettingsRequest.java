package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class GeneralSettingsRequest {
    private String siteName;
    private MultipartFile siteLogo;
    private String copyright;
}
