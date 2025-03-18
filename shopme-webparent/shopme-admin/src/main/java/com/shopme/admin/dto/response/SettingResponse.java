package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettingResponse {
    private String key;
    private String value;
}
