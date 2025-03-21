package com.shopme.admin.dto.request;

import com.shopme.common.entity.SettingValue.CurrencySymbolPosition;
import com.shopme.common.entity.SettingValue.DecimalPointType;
import com.shopme.common.entity.SettingValue.ThousandsPointType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentSettingsRequest {
    private String payosClientId;
    private String payosApiKey;
    private String payosChecksumKey;
}
