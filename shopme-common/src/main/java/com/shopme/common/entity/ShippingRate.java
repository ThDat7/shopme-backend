package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipping_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private float rate;
	private int days;
	
	@Column(name = "cod_supported")
	private boolean codSupported;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	@Column(nullable = false, length = 45)
	private String state;
}
