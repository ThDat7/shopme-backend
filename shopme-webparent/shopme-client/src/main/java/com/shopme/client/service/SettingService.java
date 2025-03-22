package com.shopme.client.service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

import java.util.List;

public interface SettingService {

    List<Setting> getSettingsByCategory(SettingCategory category);
}
