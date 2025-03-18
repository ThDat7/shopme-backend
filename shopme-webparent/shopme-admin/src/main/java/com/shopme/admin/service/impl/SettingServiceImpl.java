package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.CurrencySettingsRequest;
import com.shopme.admin.dto.request.GeneralSettingsRequest;
import com.shopme.admin.dto.request.OtherSettingRequest;
import com.shopme.admin.dto.response.CurrencySelectResponse;
import com.shopme.admin.dto.response.SettingResponse;
import com.shopme.admin.mapper.SettingMapper;
import com.shopme.admin.repository.CurrencyRepository;
import com.shopme.admin.repository.SettingRepository;
import com.shopme.admin.service.FileUploadService;
import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.SettingKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;
    private final CurrencyRepository currencyRepository;
    private final FileUploadService fileUploadService;
    private final SettingMapper settingMapper;

    @Override
    public List<SettingResponse> listBySettingCategory(SettingCategory category) {
        return settingRepository.findAllByCategory(category).stream()
                .map(s -> {
                    SettingResponse settingResponse = settingMapper.toSettingResponse(s);
                    if (s.getKey().equals(SettingKey.SITE_LOGO.name())) {
                        String logoPath = fileUploadService.getSiteLogoUrl(s.getValue());
                        settingResponse.setValue(logoPath);
                    }
                    return settingResponse;
                }).collect(Collectors.toList());

    }

    @Override
    public List<SettingCategory> listSettingCategories() {
        return Arrays.stream(SettingCategory.values())
                .collect(Collectors.toList());
    }

    @Override
    public List<SettingResponse> updateGeneralSettings(GeneralSettingsRequest generalSettingsRequest) {
        MultipartFile siteLogoFile = generalSettingsRequest.getSiteLogo();

        Setting siteLogoSetting = Setting.builder()
                .key(SettingKey.SITE_LOGO)
                .category(SettingCategory.GENERAL)
                .build();

        if (siteLogoFile != null && !siteLogoFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(siteLogoFile.getOriginalFilename()));
            fileUploadService.siteLogoUpload(siteLogoFile);
            siteLogoSetting.setValue(fileName);
        }

        List<Setting> generalSettings = settingMapper.toSettings(generalSettingsRequest);
        generalSettings.add(siteLogoSetting);
        settingRepository.saveAll(generalSettings);

        return generalSettings.stream()
                .map(settingMapper::toSettingResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<SettingResponse> updateCurrencySettings(CurrencySettingsRequest currencySettingsRequest) {
        List<Setting> currencySettings = settingMapper.toSettings(currencySettingsRequest);
        settingRepository.saveAll(currencySettings);

        return currencySettings.stream()
                .map(settingMapper::toSettingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SettingResponse> saveOtherSettings(Set<OtherSettingRequest> request) {
        List<Setting> otherSettings = settingRepository.findAllByCategory(SettingCategory.OTHER);
        Set<String> requestKeys = request.stream()
                .map(OtherSettingRequest::getKey)
                .collect(Collectors.toSet());

        List<Setting> toDelete = otherSettings.stream()
                .filter(setting -> !requestKeys.contains(setting.getKey()))
                .toList();
        settingRepository.deleteAll(toDelete);

        List<Setting> toSave = request.stream()
                .map(settingMapper::toSetting)
                .toList();
        settingRepository.saveAll(toSave);

        return toSave.stream()
                .map(settingMapper::toSettingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrencySelectResponse> listCurrencies() {
        return currencyRepository.findAll().stream()
                .map(settingMapper::toCurrencySelectResponse)
                .collect(Collectors.toList());
    }
}
