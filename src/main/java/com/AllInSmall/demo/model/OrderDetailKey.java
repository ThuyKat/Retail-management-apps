package com.AllInSmall.demo.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailKey implements Serializable {
	
	private static final long serialVersionUID = 1L; //????

	@Column(name="order_id")
	private int orderId;
	
	@Column(name="product_id")
	private int productId;
	
	// standard constructor, getters, setters done via loombok
	// hashcode and equals implementation done via loombok
	
}
