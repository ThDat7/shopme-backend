package com.shopme.client.repository;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {

    List<Setting> findAllByCategory(SettingCategory category);

    Optional<Setting> findByKey(String key);
}
