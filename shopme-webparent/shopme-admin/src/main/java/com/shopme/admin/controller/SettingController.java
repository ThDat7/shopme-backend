package com.shopme.admin.controller;

import com.shopme.admin.dto.request.CurrencySettingsRequest;
import com.shopme.admin.dto.request.GeneralSettingsRequest;
import com.shopme.admin.dto.request.OtherSettingRequest;
import com.shopme.admin.dto.response.CurrencySelectResponse;
import com.shopme.admin.dto.response.SettingResponse;
import com.shopme.admin.service.SettingService;
import com.shopme.common.dto.response.ApiResponse;
import com.shopme.common.entity.SettingCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingController {
    private final SettingService settingService;

    @GetMapping("/categories")
    public ApiResponse<List<SettingCategory>> listSettingCategories() {
        return ApiResponse.ok(settingService.listSettingCategories());
    }

    @GetMapping("/categories/{category}")
    public ApiResponse<List<SettingResponse>> listBySettingCategory(@PathVariable SettingCategory category) {
        return ApiResponse.ok(settingService.listBySettingCategory(category));
    }

    @PostMapping("/categories/general")
    public ApiResponse<List<SettingResponse>> updateGeneralSettings(@RequestBody GeneralSettingsRequest generalSettingsRequest) {
        return  ApiResponse.ok(settingService.updateGeneralSettings(generalSettingsRequest));
    }

    @PostMapping("/categories/currency")
    public ApiResponse<List<SettingResponse>> updateCurrencySettings(@RequestBody CurrencySettingsRequest currencySettingsRequest) {
        return  ApiResponse.ok(settingService.updateCurrencySettings(currencySettingsRequest));
    }

    @PostMapping("/categories/other")
    public ApiResponse<List<SettingResponse>> saveOtherSettings(@RequestBody Set<OtherSettingRequest> otherSettingRequests) {
        return  ApiResponse.ok(settingService.saveOtherSettings(otherSettingRequests));
    }

    @GetMapping("/currencies")
    public ApiResponse<List<CurrencySelectResponse>> listCurrencies() {
        return ApiResponse.ok(settingService.listCurrencies());
    }
}
