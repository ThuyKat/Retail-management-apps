package com.AllInSmall.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.enums.OrderStatus;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.User;

@Repository
public interface CustomOrderRepository {
	
	List<Order> findOrderbyFilters(String orderStatus, String username, LocalDate startDate, LocalDate endDate2,
			Float minPrice, Float maxPrice, String customerInfo);
}
