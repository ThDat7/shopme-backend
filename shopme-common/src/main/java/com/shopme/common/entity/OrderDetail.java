package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details", indexes = {
		@Index(name = "idx_order_details_order_id_product_id_quantity", columnList = "order_id, product_id, quantity"),
		@Index(name = "idx_order_details_order_id", columnList = "order_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private int quantity;
	private int productCost;
	private int shippingCost;
	private int unitPrice;
	private int subtotal;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "orderDetail")
	private Review review;
}
