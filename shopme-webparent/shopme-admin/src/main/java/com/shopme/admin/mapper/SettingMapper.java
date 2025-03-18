package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.CurrencySettingsRequest;
import com.shopme.admin.dto.request.GeneralSettingsRequest;
import com.shopme.admin.dto.request.OtherSettingRequest;
import com.shopme.admin.dto.response.CurrencySelectResponse;
import com.shopme.admin.dto.response.SettingResponse;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.SettingKey;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SettingMapper {
    SettingResponse toSettingResponse(Setting setting);

    @Mapping(target = "category", constant = "OTHER")
    @Mapping(target = "key", expression = "java(otherSettingRequest.getKey())")
    Setting toSetting(OtherSettingRequest otherSettingRequest);

    default List<Setting> toSettings(GeneralSettingsRequest generalSettingsRequest) {
        List<Setting> settings = new ArrayList<>();
        settings.add(Setting.builder()
                .key(SettingKey.SITE_NAME)
                .value(generalSettingsRequest.getSiteName())
                .category(SettingCategory.GENERAL)
                .build());

        settings.add(Setting.builder()
                .key(SettingKey.COPYRIGHT)
                .value(generalSettingsRequest.getCopyright())
                .category(SettingCategory.GENERAL)
                .build());

        return settings;
    }

    default List<Setting> toSettings(CurrencySettingsRequest currencySettingsRequest) {
        List<Setting> settings = new ArrayList<>();
        settings.add(Setting.builder()
                .key(SettingKey.CURRENCY_ID)
                .value(currencySettingsRequest.getCurrencyId().toString())
                .category(SettingCategory.CURRENCY)
                .build());

        settings.add(Setting.builder()
                .key(SettingKey.CURRENCY_SYMBOL_POSITION)
                .value(currencySettingsRequest.getCurrencySymbolPosition().toString())
                .category(SettingCategory.CURRENCY)
                .build());

        settings.add(Setting.builder()
                .key(SettingKey.DECIMAL_DIGITS)
                .value(currencySettingsRequest.getDecimalDigits().toString())
                .category(SettingCategory.CURRENCY)
                .build());

        settings.add(Setting.builder()
                .key(SettingKey.DECIMAL_POINT_TYPE)
                .value(currencySettingsRequest.getDecimalPointType().toString())
                .category(SettingCategory.CURRENCY)
                .build());

        settings.add(Setting.builder()
                .key(SettingKey.THOUSANDS_POINT_TYPE)
                .value(currencySettingsRequest.getThousandsPointType().toString())
                .category(SettingCategory.CURRENCY)
                .build());

        return settings;
    }


    CurrencySelectResponse toCurrencySelectResponse(Currency currency);
}
