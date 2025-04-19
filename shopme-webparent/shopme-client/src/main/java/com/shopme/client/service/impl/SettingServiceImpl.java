package com.shopme.client.service.impl;

import com.shopme.client.repository.SettingRepository;
import com.shopme.client.service.SettingService;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;


    @Override
    @Cacheable(value = "settingsByCategory", key = "#category.name()")
    public List<Setting> getSettingsByCategory(SettingCategory category) {
        return settingRepository.findAllByCategory(category);
    }
}
