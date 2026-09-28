package com.AllInSmall.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.dto.OrdersByStatusDto;
import com.AllInSmall.demo.dto.SalesByProductDto;
import com.AllInSmall.demo.dto.SalesByUserDto;
import com.AllInSmall.demo.enums.OrderStatus;

@Repository
public interface CustomOrderRepositoryForReporting {

	List<SalesByUserDto> findSalesByUser(LocalDate startDate, LocalDate endDate,String username);
    List<SalesByProductDto> findSalesByProduct(LocalDate startDate, LocalDate endDate);
    List<OrdersByStatusDto> findOrdersByStatus(LocalDate startDate, LocalDate endDate, OrderStatus status);
	
}
