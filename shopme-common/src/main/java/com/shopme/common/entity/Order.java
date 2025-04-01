package com.shopme.common.entity;

import java.util.*;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders", indexes = {
		@Index(name = "idx_orders_customer_id_status_order_time", columnList = "customer_id, status, order_time DESC"),
		@Index(name = "idx_orders_order_time_status", columnList = "order_time DESC, status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "first_name", nullable = false, length = 45)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 45)
	private String lastName;

	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

//	@Column(name = "address_line", nullable = false, length = 64)
	@Column(name = "address_line", length = 64)
	private String addressLine;

//	@Column(nullable = false, length = 45)
	@Column(nullable = false, length = 45)
	private String province;

	//	@Column(nullable = false, length = 45)
	@Column(nullable = false, length = 45)
	private String district;

	//	@Column(nullable = false, length = 45)
	@Column(nullable = false, length = 45)
	private String ward;

	@Column(name = "order_time")
	private Date orderTime;
	
	private float shippingCost;
	private float productCost;
	private float subtotal;
	private float tax;
	private float total;
	
	private int deliverDays;
	private Date deliverDate;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
