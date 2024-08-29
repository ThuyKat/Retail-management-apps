package com.AllInSmall.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.AllInSmall.demo.dto.OrdersByStatusDto;
import com.AllInSmall.demo.dto.SalesByProductDto;
import com.AllInSmall.demo.dto.SalesByUserDto;
import com.AllInSmall.demo.enums.OrderStatus;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.Product;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.repository.CustomOrderRepositoryForReporting;
import com.AllInSmall.demo.repository.OrderRepository;
import com.AllInSmall.demo.repository.ProductRepository;
import com.AllInSmall.demo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController {
	@Autowired
	private CustomOrderRepositoryForReporting orderRepoForReporting;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private OrderRepository orderRepository;
	
//	@Autowired
//	private ProductRepository productRepository;

	@GetMapping("/salesByUser")
	public String getSalesByUser( Model model) {
		List<User>users = userRepository.findAll();
		List<String>usernames = new ArrayList<>();
		for(User user : users) {
			usernames.add(user.getUsername());
		}
		model.addAttribute("users", usernames); 
		return "salesByUser";

	}
	

	@GetMapping("/salesByUser/data")
	@ResponseBody
	public ResponseEntity<?> getSalesByUsersData(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) String username) {

	    log.info("Fetching sales by users data");
	    try {
			List<SalesByUserDto> salesByUser = orderRepoForReporting.findSalesByUser(startDate, endDate, username);
	        return ResponseEntity.ok(salesByUser);
	    } catch (Exception e) {
	        log.error("Error processing Order By Status data", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Collections.singletonMap("error", "An error occurred while processing the data."));
	    }
	}

	@GetMapping("/salesByProduct/data")
	public ResponseEntity<?> getSalesByProductData(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, Model model) {
		 log.info("Fetching Order By Product data");
		try {
		List<SalesByProductDto> salesByProduct = orderRepoForReporting.findSalesByProduct(startDate, endDate);
		return ResponseEntity.ok(salesByProduct);
    } catch (Exception e) {
        log.error("Error processing Order By Product data", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Collections.singletonMap("error", "An error occurred while processing the data."));
    }
	}
	
	@GetMapping("/salesByProduct")
	public String getSalesByProductPage(Model model) {
	    log.info("Loading Order By Status report page");
//	    List<Product> products = productRepository.findAll();
//	    List<String> productIdAndNames = new ArrayList<>();
//	    for (Product product : products) {
//	    	String productIdAndName = product.getId() +"-"+ product.getName();
//	    	productIdAndNames.add(productIdAndName);
//	    }
//	    model.addAttribute("products",productIdAndNames);
	    return "salesByProduct";
	}
	
	@GetMapping("/ordersByStatus")
	public String getOrdersByStatusPage(Model model) {
	    log.info("Loading Order By Status report page");
	    model.addAttribute("statuses", OrderStatus.values());
	    return "ordersByStatus";
	}
	
	@GetMapping("/ordersByStatus/data")
	@ResponseBody
	public ResponseEntity<?> getOrdersByStatusData(
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	    @RequestParam(required = false) OrderStatus status) {

	    log.info("Fetching Order By Status data");
	    try {
	        List<OrdersByStatusDto> ordersByStatus = orderRepoForReporting.findOrdersByStatus(startDate, endDate, status);
	        return ResponseEntity.ok(ordersByStatus);
	    } catch (Exception e) {
	        log.error("Error processing Order By Status data", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Collections.singletonMap("error", "An error occurred while processing the data."));
	    }
	}
	
	
	@GetMapping("/ordersByStatus/details")
	public String getOrderDetails(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) OrderStatus status,
            Model model) {
		List<Order> orders = orderRepository.findByDateBetweenAndStatus(startDate.atStartOfDay(),endDate.atTime(LocalTime.MAX),status);
		model.addAttribute("orders", orders);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
	    model.addAttribute("status", status);
	    
	    return "detailsOrderReport";
	}
	

}
