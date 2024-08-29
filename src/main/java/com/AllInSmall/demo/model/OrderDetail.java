package com.AllInSmall.demo.model;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude= {"product","order"})
public class OrderDetail  {

@EmbeddedId
OrderDetailKey id;

@ManyToOne
@JsonBackReference
@MapsId("productId") // attribute of OrderDetailKey
@JoinColumn(name="product_id")
private Product product;

@ManyToOne
@JsonBackReference
@MapsId("orderId") // attribute of OrderDetailKey
@JoinColumn(name="order_id")
private Order order;

private int quantity;

private double price;

@Column(name="created_by")	
private String createdBy;

@Column(name="created_date")
private LocalDateTime createdDate;

//@Column(name="modify_by")
//private String modifiedBy;
//
//@Column(name="modify_date")
//private LocalDateTime modifiedDate;




}
