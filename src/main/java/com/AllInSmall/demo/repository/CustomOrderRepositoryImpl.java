package com.AllInSmall.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.enums.OrderStatus;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

//@Qualifier("impl1")
@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

	@Autowired
	EntityManager entityManager;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public List<Order> findOrderbyFilters(String orderStatus, String username, LocalDate startDate, LocalDate endDate,
			Float minPrice, Float maxPrice, String customerInfo) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		// INITIALIZE QUERY
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

		// FROM WHICH CLASS THE QUERY IS PERFORMED ON
		Root<Order> orderRoot = criteriaQuery.from(Order.class);

		// BUILDING LIST OF TESTS FOR WHERE STATEMENT
		List<Predicate> predicates = new ArrayList<>();

		// ORDER STATUS
		if (orderStatus != null && !orderStatus.isEmpty() ) {
			Predicate statusPredicate = criteriaBuilder.equal(orderRoot.get("status"), OrderStatus.valueOf(orderStatus));
			predicates.add(statusPredicate);
		}

		// DATE RANGE
		if (startDate != null && endDate != null) {
			Predicate datePredicate = criteriaBuilder.between(orderRoot.get("date"), startDate, endDate);
			predicates.add(datePredicate);
		} else if (startDate != null) {
			Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(orderRoot.get("date"), startDate);
			predicates.add(startDatePredicate);
		} else if (endDate != null) {
			Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(orderRoot.get("date"), endDate);
			predicates.add(endDatePredicate);
		}
		
		// PRICE RANGE
	    if (minPrice != null && maxPrice != null) {
	        Predicate pricePredicate = criteriaBuilder.between(orderRoot.get("totalPrice"), minPrice, maxPrice);
	        predicates.add(pricePredicate);
	    } else if (minPrice != null) {
	        Predicate minPricePredicate = criteriaBuilder.greaterThanOrEqualTo(orderRoot.get("totalPrice"), minPrice);
	        predicates.add(minPricePredicate);
	    } else if (maxPrice != null) {
	        Predicate maxPricePredicate = criteriaBuilder.lessThanOrEqualTo(orderRoot.get("totalPrice"), maxPrice);
	        predicates.add(maxPricePredicate);
	    }
	    
	    // USERNAME
	    if (username != null && !username.isEmpty()) {
	    	Optional<User> userOptional = userRepository.findByUsername(username);
	    	if(userOptional.isPresent()) {
	    	User user = userOptional.get();
	        Predicate userPredicate = criteriaBuilder.equal(orderRoot.get("user"), user);
	        predicates.add(userPredicate);}
	    }
	    
	    // CUSTOMER INFO
	    if (customerInfo != null && !customerInfo.isEmpty()) {
	        Predicate customerInfoPredicate = criteriaBuilder.like(orderRoot.get("customerInfo"), "%" + customerInfo + "%");
	        predicates.add(customerInfoPredicate);
	    }
	    
		 
		// Combine if filters are provided
		    if (!predicates.isEmpty()) {
		        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		    }
		 // Execute query: select is understood without explicit declarations
		    return entityManager.createQuery(criteriaQuery).getResultList();

	}

}
