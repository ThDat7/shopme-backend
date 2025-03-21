package com.shopme.common.entity;

import com.shopme.common.entity.SettingValue.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SettingKey {
    // General Settings
    SITE_NAME(SettingCategory.GENERAL),
    SITE_LOGO(SettingCategory.GENERAL),
    COPYRIGHT(SettingCategory.GENERAL),

    // Currency Settings
    CURRENCY_ID(SettingCategory.CURRENCY),
    CURRENCY_SYMBOL(SettingCategory.CURRENCY),
    CURRENCY_SYMBOL_POSITION(SettingCategory.CURRENCY),
    DECIMAL_DIGITS(SettingCategory.CURRENCY),
    DECIMAL_POINT_TYPE(SettingCategory.CURRENCY),
    THOUSANDS_POINT_TYPE(SettingCategory.CURRENCY),

    // Payment Settings
    PAYOS_CLIENT_ID(SettingCategory.PAYMENT),
    PAYOS_API_KEY(SettingCategory.PAYMENT),
    PAYOS_CHECKSUM_KEY(SettingCategory.PAYMENT);

    private final SettingCategory category;
}