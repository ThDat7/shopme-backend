package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtherSettingRequest {
    private String key;
    private String value;
}
