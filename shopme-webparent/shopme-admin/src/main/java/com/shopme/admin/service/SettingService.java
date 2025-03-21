package com.shopme.admin.service;

import com.shopme.admin.dto.request.CurrencySettingsRequest;
import com.shopme.admin.dto.request.GeneralSettingsRequest;
import com.shopme.admin.dto.request.OtherSettingRequest;
import com.shopme.admin.dto.request.PaymentSettingsRequest;
import com.shopme.admin.dto.response.CurrencySelectResponse;
import com.shopme.admin.dto.response.SettingResponse;
import com.shopme.common.dto.response.ApiResponse;
import com.shopme.common.entity.SettingCategory;

import java.util.List;
import java.util.Set;

public interface SettingService {
    List<SettingResponse> listBySettingCategory(SettingCategory category);

    List<SettingCategory> listSettingCategories();

    List<SettingResponse> updateGeneralSettings(GeneralSettingsRequest generalSettingsRequest);

    List<SettingResponse> updateCurrencySettings(CurrencySettingsRequest currencySettingsRequest);

    List<SettingResponse> saveOtherSettings(Set<OtherSettingRequest> otherSettingRequests);

    List<CurrencySelectResponse> listCurrencies();

    List<SettingResponse> updatePaymentSettings(PaymentSettingsRequest paymentSettingsRequest);
}
