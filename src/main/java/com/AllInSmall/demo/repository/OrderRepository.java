package com.AllInSmall.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.enums.OrderStatus;
import com.AllInSmall.demo.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>{

	Order findByPaypalId(String paypalOrderId);
	
	@SuppressWarnings("unchecked")
	Order save(Order order);
	
	List<Order> findByDateBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status);

}
