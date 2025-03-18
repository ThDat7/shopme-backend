package com.shopme.admin.dto.request;

import com.shopme.common.entity.SettingValue.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencySettingsRequest {
    private Integer currencyId;
    private CurrencySymbolPosition currencySymbolPosition;
//    jakarta validation later
    private Integer decimalDigits;
    private DecimalPointType decimalPointType;
    private ThousandsPointType thousandsPointType;
}
