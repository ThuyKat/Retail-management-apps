package com.AllInSmall.demo.controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.AllInSmall.demo.dto.AddOrderDetailRequest;
import com.AllInSmall.demo.dto.OrderDetailRequestWrapper;
import com.AllInSmall.demo.model.Category;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.OrderDetail;
import com.AllInSmall.demo.model.Product;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.repository.CategoryRepository;
import com.AllInSmall.demo.repository.ProductRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.Model;
@Slf4j
@Controller
public class HomeController {

	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;
	private final Order sessionOrder;

	@Autowired
	public HomeController(CategoryRepository categoryRepository, ProductRepository productRepository,
			 @Qualifier("sessionOrder") Order sessionOrder) {

		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
		this.sessionOrder = sessionOrder;

	}

	@GetMapping("/management")
	public String getManagementDashboard(Model model,Principal principal ) {
		User user = sessionOrder.getUser();
		model.addAttribute("userRole", user.getRole().getRoleName());
		model.addAttribute("username", principal.getName());
		
		return "ownerHome";
		
	}
	@GetMapping("/index")
	public String getAllCategories(@RequestParam(required = false) Integer categoryId,
			@RequestParam(required = false) String action, Model model, Principal principal) {
		log.info("number of items in order: "+String.valueOf(sessionOrder.getItemCount()));

		List<Category> allCategories = categoryRepository.findAll();
		List<Category> showCategories = new ArrayList<>();
		for(Category category : allCategories) {
			if(!category.getProducts().isEmpty()) {
				showCategories.add(category);
			}
		}
		OrderDetailRequestWrapper orderWrapper = new OrderDetailRequestWrapper();
		ArrayList<AddOrderDetailRequest> orderItems = new ArrayList<>();
		List<Product>products;
		Set<OrderDetail>orderDetails = sessionOrder.getOrderDetails();
		
		if (categoryId != null) {
			products = productRepository.findProductByCategoryId(categoryId);

		} else {
			products = productRepository.findAll();

		}
		for (Product product:products) {
			//find the existing orderDetail in orderDetails that contains the selected product
			//keep the chosen quantity so when page is redirect user can adjust product quantity for further updates
			int quantity = 0;
			if (orderDetails !=null) {
			OrderDetail orderDetailContainsProduct = orderDetails.stream().filter(od -> od.getProduct().getId().equals(product.getId()))
			        .findAny()
			        .orElse(null);
			
				if (orderDetailContainsProduct != null) {
					 quantity = orderDetailContainsProduct.getQuantity();
				}
			}
			
			AddOrderDetailRequest orderItem = new AddOrderDetailRequest(product,quantity);
			orderItems.add(orderItem);
		}
		orderWrapper.setOrderItems(orderItems);
		
		model.addAttribute("allCategory", showCategories);
		model.addAttribute("orderWrapper", orderWrapper);
		model.addAttribute("username", principal.getName());
		model.addAttribute("itemCount",sessionOrder.getItemCount());
		return "index";
	}
     @GetMapping("/navigate")
     public String navigateToPreviousPage(HttpSession session,HttpServletRequest request) {
    	 Stack<String>navigationStack = (Stack<String>) session.getAttribute("navigationStack");
    	 navigationStack.pop();
    	 String returnPage = navigationStack.peek(); 
    	 session.setAttribute("navigationStack", navigationStack);
    	 return "redirect:"+returnPage;
     }
     
	
	}


