package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
	@Id
	@Column(name = "`key`", nullable = false, length = 128)
	private String key;
	
	@Column(nullable = false, length = 1024)
	private String value;

	@Enumerated(EnumType.STRING)
	@Column(length = 128, nullable = false)
	private SettingCategory category;

	public void setKey(String key) {
		this.key = key;
	}

	public void setKey(SettingKey key) {
		this.key = key.name();
	}

	@Builder
	public Setting(SettingKey key, String value, SettingCategory category) {
		this.key = key.name();
		this.value = value;
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Setting setting = (Setting) o;
		return Objects.equals(key, setting.key);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(key);
	}
}
