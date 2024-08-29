package com.AllInSmall.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Set;

import com.AllInSmall.demo.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Enumerated(EnumType.STRING) // if not specify, default will be ordinal 0,1,2
	private OrderStatus status;

	private String paypalId;

	private LocalDateTime date;// created_at date

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "total_price")
	private float totalPrice;

	@OneToMany(mappedBy = "order") // name of object Order in OrderDetail
	@JsonManagedReference
	private Set<OrderDetail> orderDetails;

	@ManyToOne(fetch = FetchType.LAZY) // default type is eager
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;
	/*
	 * FetchType.LAZY: The User object is loaded on demand. When you first retrieve
	 * the Order entity, the User object is not immediately loaded. It is fetched
	 * from the database only when you access order.getUser(). FetchType.EAGER: The
	 * User object is loaded immediately along with the Order entity. This can lead
	 * to unnecessary data being loaded if you don't always need the User object.
	 */
	@OneToMany(mappedBy = "order") // name of object Order in Comment
	@JsonManagedReference
	private Set<Comment> comments;

	@Column(columnDefinition = "TEXT")
	private String customerInfo;

	@Column(columnDefinition = "TEXT")
	private String orderNote;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	/*
	 * CascadeType.all -> associate this with order so when status of order is
	 * updated this will also be updated save order-> cascade.persist; update order
	 * -> cascade.refresh; delete order -> cascade.remove
	 * entityManager.detach(order) then order.setStatus(OrderStatus.DELIVERED); ->
	 * changes are no longer tracked by persistent context-> cascade detach
	 * entityManager.merge(order) -> bring back into managed state -> cascade merge
	 * orphanRemoval = true : add and remove elements of this list directly affects
	 * OrderStatusHistory entities
	 */
	private List<OrderStatusHistory> statusHistory = new ArrayList<>();

	public void reset() {
		if (this.orderDetails != null) {
			this.status = null;
			this.orderDetails.clear();
			this.totalPrice = 0;
		}
	}

	public int getItemCount() {
		return this.orderDetails.size();
	}

}