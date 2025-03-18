package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.GeneralSettingsRequest;
import com.shopme.admin.dto.response.SettingResponse;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingKey;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SettingMapper {
    SettingResponse toSettingResponse(Setting setting);

    default List<Setting> toSettings(GeneralSettingsRequest generalSettingsRequest) {
        List<Setting> settings = new ArrayList<>();
        settings.add(Setting.builder()
                .key(SettingKey.SITE_NAME)
                .value(generalSettingsRequest.getSiteName())
                .build());

        settings.add(Setting.builder()
                .key(SettingKey.COPYRIGHT)
                .value(generalSettingsRequest.getCopyright())
                .build());

        return settings;
    }
}
