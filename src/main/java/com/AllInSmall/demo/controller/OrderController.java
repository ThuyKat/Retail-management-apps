package com.AllInSmall.demo.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AllInSmall.demo.model.Comment;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.OrderDetail;
import com.AllInSmall.demo.model.OrderDetailKey;
import com.AllInSmall.demo.model.Product;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.dto.AddOrderDetailRequest;
import com.AllInSmall.demo.dto.OrderDetailRequestWrapper;
import com.AllInSmall.demo.dto.OrderToViewRequest;
import com.AllInSmall.demo.dto.PaypalOrderResponse;
import com.AllInSmall.demo.enums.OrderStatus;
import com.AllInSmall.demo.exception.ProductNotFoundException;
import com.AllInSmall.demo.repository.CommentRepository;
import com.AllInSmall.demo.repository.CustomOrderRepository;
import com.AllInSmall.demo.repository.OrderDetailRepository;
import com.AllInSmall.demo.repository.OrderRepository;
import com.AllInSmall.demo.repository.ProductRepository;
import com.AllInSmall.demo.repository.UserRepository;
import com.AllInSmall.demo.service.PayPalService;
import com.google.zxing.WriterException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

	private final Order sessionOrder;
	private OrderRepository orderRepository;
	private OrderDetailRepository orderDetailRepository;
	private PayPalService payPalService;
	private CommentRepository commentRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private CustomOrderRepository customOrderRepository;

	@Autowired
	public OrderController(@Qualifier("sessionOrder") Order sessionOrder, OrderRepository orderRepository,
			OrderDetailRepository orderDetailRepository, PayPalService payPalService,
			CommentRepository commentRepository, UserRepository userRepository, ProductRepository productRepository,CustomOrderRepository customOrderRepository) {
		this.sessionOrder = sessionOrder;
		this.orderRepository = orderRepository;
		this.payPalService = payPalService;
		this.orderDetailRepository = orderDetailRepository;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.customOrderRepository = customOrderRepository;
	}

	
//	, produces = MediaType.IMAGE_PNG_VALUE -> remove this
	@GetMapping
	public String viewOrderOrResetOrder(Model model, @RequestParam(name = "action", required = false) String action,
			RedirectAttributes redirectAttributes) {
		if ("viewOrder".equals(action)) {
			Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
			if (orderDetails != null) {
				double totalPrice = orderDetails.stream()
						.mapToDouble(od -> od.getQuantity() * od.getProduct().getPrice()).sum();
				float roundedTotal = Math.round(totalPrice * 100) / 100;
				sessionOrder.setTotalPrice(roundedTotal);
			}
			model.addAttribute("order", sessionOrder);
			return "viewOrder";
		} else if ("resetOrder".equals(action)) {
			// clear the session-scoped Order bean
			try {
				sessionOrder.reset();
				// Set a flash attribute message
				redirectAttributes.addFlashAttribute("message", "Order is reset!");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error resetting order", e);
				redirectAttributes.addFlashAttribute("error", "There was an error resetting the order.");

			}

		}

		// Redirect to the same page
		return "redirect:/index";

	}


	@GetMapping("/viewOrderList")
	public String showOrderList(Model model, HttpSession session, HttpServletRequest request, @RequestParam(name="action",required=false)String action,@RequestParam(name="orderId",required=false) Integer orderId) {
		if("viewSavedOrder".equalsIgnoreCase(action)) {
			log.info("I am viewing individual order");
			viewSavedOrderOnOrderList(  orderId, model);
			return "updateOrderStatusAndNotes";
		}
		log.info("I am not viewing individual order");
		
		// get list of order from database
		List<Order> ordersDB = orderRepository.findAll();// return an empty list if there are no records in the database
															// table. It will not return null.
		Set<OrderToViewRequest> orderToViews = new TreeSet<>();
		for (Order order : ordersDB) {
			// find payment method
			String paymentMethod = "CASH";
			if (order.getPaypalId() == null) {
				paymentMethod = "NULL";
			} else if (!order.getPaypalId().equalsIgnoreCase("CASH")) {
				paymentMethod = "PAYPAL";
			}
			OrderToViewRequest orderToView = OrderToViewRequest.builder().customerInfo(order.getCustomerInfo())
					.paymentMethod(paymentMethod).status(order.getStatus()).totalValue(order.getTotalPrice())
					.orderDateTime(order.getDate()).Id(order.getId()).build();
			orderToViews.add(orderToView);

		}
		model.addAttribute("orderList", orderToViews);
//		// Capture the referer URL
//        String referer = request.getHeader("Referer");
//        log.info("This is refere in header of request: "+referer);
//     // Determine the source page and set a flag in the session
//        if (referer != null) {
//            if (referer.contains("/confirm")) {
//                session.setAttribute("sourcePage", "confirm");
//            } else if (referer.contains("/manageOrder")) {
//                session.setAttribute("sourcePage", "manageOrder");
//            }
//        }
		return "viewOrderList";

	}

	
	private void viewSavedOrderOnOrderList( int orderId,Model model) {
		// order section
		Order order = null;
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isPresent()) {
			order = orderOptional.get();
			model.addAttribute("order", order);
			model.addAttribute("currentStatus", order.getStatus());
			// order details section
			Set<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
			model.addAttribute("orderDetails", orderDetails);

			// order statuses section
			OrderStatus[] orderStatuses = OrderStatus.values();
			model.addAttribute("orderStatuses", orderStatuses);

			// comment section
			List<Comment> comments = commentRepository.findByOrderId(orderId);

			// Create a map to store comment IDs and corresponding usernames
			Map<Integer, String> commentUsernames = new HashMap<>();
			for (Comment comment : comments) {

				User user = comment.getUser();
				String username = user.getUsername();
				commentUsernames.put(comment.getId(), username);
			}

			model.addAttribute("comments", comments);
			model.addAttribute("commentUsernames", commentUsernames);

		} else {
			// Handle the case where the order is not found
			log.info("Order with ID " + orderId + " not found.");
			// Optionally, add an error message to the model
			model.addAttribute("errorMessage", "Order not found.");
		}

		if (sessionOrder.getUser() != null) {
			model.addAttribute("userId", sessionOrder.getUser().getId());
		} else {
			model.addAttribute("userId", 2);
		}
		model.addAttribute("orderId", orderId);
		
	}
	

    @GetMapping("/placeOrder")
    public String handlePlaceOrderGet(@RequestParam(name="action",required = false,defaultValue="noAction") String action,
                                      @RequestParam(required = false) String token,
                                      @RequestParam(required = false) String PayerID,
                                      Model model) throws WriterException, IOException {
        switch (action) {
            case "paypal-create":
                return createPayPalOrder(model);
            case "paypal-confirm":
                return handlePayPalReturn(token, PayerID, model);
            case "paypal-cancel":
                return handlePayPalCancel(model);
            case "cash-confirm":
                return handleCashConfirmation(model);
            case "cash-pay":
                return showCashPayment(model);
            default:
                return "redirect:/order/viewOrder";
        }
    }
	
	
	
	@PostMapping("/placeOrder")
    public String handlePlaceOrderPost(@RequestParam String action,
                                       @RequestParam(name="customerInfo",required = false) String customerInfo,
                                       @RequestParam(name="orderNote",required = false) String orderNote
                                      ) {
        if ("select-payment".equals(action)) {
            return showPaymentOptions(customerInfo, orderNote);
        }
        return "redirect:/order/viewOrder";
    }
	private String showPaymentOptions( String customerInfo, String orderNote) {
		// save customer info and order notes to session
		sessionOrder.setOrderNote(orderNote);
		sessionOrder.setCustomerInfo(customerInfo);
		// if order is not empty then redirect to payment option else stay at the same
		// page
		if (!sessionOrder.getOrderDetails().isEmpty()) {
			return "paymentOption"; // This will resolve to /WEB-INF/views/paymentOptions.jsp
		} else {
			return "/order/viewOrder";
		}
	}
	


	@PostMapping("/viewSavedOrder")
	public String updateOrder(@RequestParam(value = "action", required = false) String action,
			@RequestParam("subject") String subject, @RequestParam("commentText") String commentText,
			@RequestParam("userId") int userId, @RequestParam("orderId") int orderId,
			@RequestParam("orderStatus") OrderStatus status, Model model) {
		if ("addComment".equals(action)) {
			log.info("I am in add comment action");
			log.info("userId " + userId);
			log.info("orderId " + orderId);
			Optional<User> userOptional = userRepository.findById(userId);
			Optional<Order> orderOptional = orderRepository.findById(orderId);
			if (userOptional.isPresent() && orderOptional.isPresent()) {
				log.info("I am in add comment method if");
				Order order = orderOptional.get();
				User user = userOptional.get();
				// save comment to db
				log.info(commentText);
				log.info(subject);
				log.info("userId " + userId);
				Comment comment = Comment.builder().subject(subject).commentText(commentText).user(user).order(order)
						.build();
				log.info(comment + "is being saved");
				commentRepository.save(comment);

			}
		} else if ("updateOrderStatus".equals(action)) {
			Optional<Order> orderOptional = orderRepository.findById(orderId);
			if (orderOptional.isPresent()) {
				Order order = orderOptional.get();
				order.setStatus(status);
				order.setUpdatedAt(LocalDateTime.now());
				orderRepository.save(order);
			}
		}

		return "redirect:/order/viewSavedOrder?orderId=" + orderId;
	}


	@PostMapping
	public String addToOrder(@ModelAttribute(name = "orderWrapper") OrderDetailRequestWrapper orderWrapper,
			RedirectAttributes redirectAttributes, Model model,
			@RequestParam(name = "action", required = true) String action) {
		if ("addToOrder".equals(action)) {
			log.info("addToOrder method called");

			try {
				// new order instance
//			for (Map.Entry<Integer, Integer> orderItem : orderItems.entrySet()) {
				for (AddOrderDetailRequest orderItem : orderWrapper.getOrderItems()) {
					// get id and quantity of each product
					int productId = orderItem.getProduct().getId();
					int quantity = orderItem.getQuantity();
					if (quantity > 0) {

						// find the product in DB by productId
						Product product = productRepository.findById(productId)
								.orElseThrow(() -> new ProductNotFoundException("Product not found"));

//				OTHER OPTON: 
//
//		            Product product = productRepository.findById(productId)
//		                .orElse(null);
//
//		            if (product == null) {
						// print message..
//		                continue; // Skip this item and move to the next
//		            }

						updateOrCreateOrderDetail(product, quantity);
						// product is not found

//				else {
//					return ResponseEntity.ok("Nothing is added, no product is found");
//				} // replace with exception handling
//				message = "something is added " + orderItem.getProduct().getName();

					} else {
						// find if product is in sessionOrder -> remove product from sessionOrder
						Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
						if (orderDetails != null) {
							OrderDetail orderDetailContainsProduct = orderDetails.stream()
									.filter(od -> od.getProduct().getId().equals(productId)).findAny().orElse(null);

							if (orderDetailContainsProduct != null) {
								orderDetails.remove(orderDetailContainsProduct);
							}
						}

					}
				}
				log.info("number of items in order: " + String.valueOf(sessionOrder.getItemCount()));

				// Set a flash attribute message
				redirectAttributes.addFlashAttribute("message", "updated Order");

			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error updating order", e);
				redirectAttributes.addFlashAttribute("error", "There was an error updating the order.");
			}
		}
		// Redirect to the same page
		return "redirect:/index";

	}
	
	@GetMapping("/search")
	public String showSearchForm(Model model) {
	    model.addAttribute("orderStatuses", OrderStatus.values());
	    model.addAttribute("usernames", getAllUsernames());
	    return "orderSearchForm";
	}
	
	
	/*
	 * GetMapping: not modify resource, makes it easy to share search results by sharing the URL -> better using Get mapping
	 * @DateTimeFormat(iso = DateTimeFormat.ISO.DATE): only the date part of the ISO standard should be used, without time information.
	 */
	@GetMapping("/search/result")
	public String searchOrders(@RequestParam(required = false) String orderStatus,
	                           @RequestParam(required = false) String username,
	                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
	                           @RequestParam(required = false) Float minPrice,
	                           @RequestParam(required = false) Float maxPrice,
	                           @RequestParam(required = false) String customerInfo,
	                           Model model) {
	    List<Order> results = customOrderRepository.findOrderbyFilters(orderStatus, username, startDate, endDate, minPrice, maxPrice, customerInfo);
	    model.addAttribute("orders", results);
	    model.addAttribute("isSubmit", true);
	    return "orderSearchForm";
	}
	
	/*
	 * ----------------------------	PRIVATE METHOD SECTION----------------------------
	 */

	
	private List<String> getAllUsernames(){
		List<User> users = userRepository.findAll();
		List<String>usernameList = new ArrayList<>();
		for(User user : users) {
			usernameList.add(user.getUsername());
		}
		return usernameList;
		
	}
	private void updateOrCreateOrderDetail(Product product, int quantity) {
		Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
		// product is found, check if sessionOrder has any orderDetails

		if (orderDetails == null) {
			orderDetails = new HashSet<>();
			sessionOrder.setOrderDetails(new HashSet<>());
		}

		// find the existing orderDetail in orderDetails that contains the selected
		// product
		OrderDetail existingOrderDetail = orderDetails.stream()
				.filter(od -> od.getProduct().getId().equals(product.getId())).findAny().orElse(null);
		// if the existing orderDetail is not found
		if (existingOrderDetail == null) {

			// Create OrderDetailKey
			OrderDetailKey orderDetailKey = new OrderDetailKey(sessionOrder.getId(), product.getId());

			// new order and orderDetail instance

			OrderDetail orderDetail = new OrderDetail();
			// build orderDetail
			orderDetail = OrderDetail.builder().id(orderDetailKey) // Set the composite key
					.product(product).quantity(quantity).price(product.getPrice()).order(sessionOrder).createdBy("thuy")
					.createdDate(LocalDateTime.now()).build();

			// Add the new OrderDetail
			sessionOrder.getOrderDetails().add(orderDetail);

			System.out.println("added orderDetail: " + orderDetail.getProduct().getName());

		} else {
			Integer updatedQuantity = quantity;

			existingOrderDetail.setQuantity(updatedQuantity);

		}
	}
	private String showCashPayment(Model model) {
		Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
		if (orderDetails != null) {
			double totalPrice = orderDetails.stream().mapToDouble(od -> od.getQuantity() * od.getProduct().getPrice())
					.sum();
			float roundedTotal = Math.round(totalPrice * 100) / 100;
			sessionOrder.setTotalPrice(roundedTotal);
		}
		model.addAttribute("order", sessionOrder);
		return "cashPayment"; // This will resolve to /WEB-INF/views/cashPayment.jsp
	}
	
	private String handleCashConfirmation(Model model) {
		sessionOrder.setPaypalId("CASH");
		saveOrderToDatabase();
		model.addAttribute("message", "Payment successful, new order is saved.");
		String orderId = UUID.randomUUID().toString();
		model.addAttribute("orderId", orderId);
		return "orderConfirmation";
	}
	private String handlePayPalCancel(Model model) {
		model.addAttribute("message", "Payment was cancelled.");
		log.info("current session order :" + sessionOrder.toString());
		return "paymentCancelled";
	}
	private String handlePayPalReturn(@RequestParam String token, @RequestParam String PayerID, Model model) {
		try {
			// Capture the payment using the token
			PaypalOrderResponse response = payPalService.captureOrderByToken(token);

			// Process the response
			if ("COMPLETED".equals(response.getStatus())) {
				// Payment was successful
				// Update your order in the database
				String paypalOrderId = response.getId();
				sessionOrder.setPaypalId(paypalOrderId);
				saveOrderToDatabase();
				model.addAttribute("message", "Payment successful, new order is saved.");
				model.addAttribute("orderId", paypalOrderId);

			} else {
				// Payment failed or is in a pending state
				model.addAttribute("message", "Payment not completed. Status: " + response.getStatus());

			}
		} catch (Exception e) {
			// Handle any exceptions
			model.addAttribute("message", "Error processing payment: " + e.getMessage());
		}

		return "orderConfirmation"; //
	}
	private String createPayPalOrder(Model model) throws WriterException, IOException {
		try {
			double orderValue = sessionOrder.getTotalPrice();
			byte[] qrCodeBytes = payPalService.createOrderWithQRCode(String.valueOf(orderValue));
			String base64Image = Base64.getEncoder().encodeToString(qrCodeBytes);
			model.addAttribute("qrCodeImage", base64Image);
		} catch (Exception e) {
			model.addAttribute("error", "Failed to generate QR code: " + e.getMessage());
		}
		return "paypalQRCode";

	}// Use @Transactional to ensure atomicity
	@Transactional
	private ResponseEntity<String> saveOrderToDatabase() {
		Order savedOrder = null;
		try {
			if (sessionOrder == null || sessionOrder.getOrderDetails() == null
					|| sessionOrder.getOrderDetails().isEmpty()) {
				return ResponseEntity.badRequest().body("No items in the order");
			}
			// save orderDetails and order to database

			// save the order
			sessionOrder.setStatus(OrderStatus.PLACED);
			sessionOrder.setDate(LocalDateTime.now()); // Set the order date

			Order newOrder = new Order();
			newOrder.setStatus(sessionOrder.getStatus());
			newOrder.setPaypalId(sessionOrder.getPaypalId());
			newOrder.setTotalPrice(sessionOrder.getTotalPrice());
			newOrder.setUser(sessionOrder.getUser());
			newOrder.setDate(sessionOrder.getDate());
			newOrder.setCustomerInfo(sessionOrder.getCustomerInfo());
			newOrder.setOrderNote(sessionOrder.getOrderNote());

			savedOrder = orderRepository.save(newOrder);

			log.info(savedOrder.toString());
			if (savedOrder != null) {
				// save the order details
				Set<OrderDetail> orderDetails = sessionOrder.getOrderDetails();
				for (OrderDetail orderDetail : orderDetails) {
					orderDetail.setOrder(savedOrder);
					orderDetailRepository.save(orderDetail);
					log.info("I am saving new orderDetail to database: "+orderDetail.toString());
				}

				sessionOrder.reset();// Clear the session order after successful placement

			}

		} catch (Exception e) {
			// Log the exception (optional)
			log.error("Error placing order", e);
			return ResponseEntity.internalServerError()
					.body("An error occurred while placing the order: " + e.getMessage());
		}
		return ResponseEntity
				.ok("Order " + savedOrder.getId() + " has been placed successfully. Redirecting to order list...");

		// Redirect to the listOrder page with a query parameter

//		return "redirect:/viewOrderList?message=orderPlaced"; //NEED JAVASCRIPT TO SHOW POP-UP

	}


}
