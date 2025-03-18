package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "currencies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 64)
	private String name;
	
	@Column(nullable = false, length = 3)
	private String symbol;
	
	@Column(nullable = false, length = 4)
	private String code;
}
