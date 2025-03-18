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
}
