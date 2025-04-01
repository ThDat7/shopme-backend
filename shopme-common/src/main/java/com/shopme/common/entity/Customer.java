package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "first_name", nullable = false, length = 45)
	protected String firstName;

	@Column(name = "last_name", nullable = false, length = 45)
	protected String lastName;

	@Column(name = "phone_number", nullable = false, length = 15)
	protected String phoneNumber;
	
	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(nullable = false, length = 64)
	private String password;
	
	private boolean enabled;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 15)
	private CustomerStatus status;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type", length = 10)
	private AuthenticationType authenticationType;

	//	will move to redis cache for timeout
	@Column(name = "verification_code", length = 36)
	private String verificationCode;
}
