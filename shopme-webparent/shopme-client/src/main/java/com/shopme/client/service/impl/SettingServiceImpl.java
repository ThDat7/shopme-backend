package com.shopme.client.service.impl;

import com.shopme.client.repository.SettingRepository;
import com.shopme.client.service.SettingService;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;

    @Override
    public List<Setting> getSettingsByCategory(SettingCategory category) {
        return settingRepository.findAllByCategory(category);
    }
}
