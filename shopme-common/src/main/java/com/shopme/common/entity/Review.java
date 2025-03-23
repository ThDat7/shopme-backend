package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews", indexes = {
		@Index(name = "idx_review_order_detail_id_rating", columnList = "order_detail_id, rating"),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false)
	private String headline;
	
	@Column(length = 300, nullable = false)
	private String comment;
	
	private int rating;
	
	@Column(nullable = false)
	private Date reviewTime;

	@OneToOne
	@JoinColumn(name = "order_detail_id")
	private OrderDetail orderDetail;
}
