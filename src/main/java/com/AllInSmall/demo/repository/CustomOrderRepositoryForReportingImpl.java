package com.AllInSmall.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.dto.OrdersByStatusDto;
import com.AllInSmall.demo.dto.SalesByProductDto;
import com.AllInSmall.demo.dto.SalesByUserDto;
import com.AllInSmall.demo.enums.OrderStatus;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.OrderDetail;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CustomOrderRepositoryForReportingImpl implements CustomOrderRepositoryForReporting {

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<SalesByUserDto> findSalesByUser(LocalDate startDate, LocalDate endDate, String username) {
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<SalesByUserDto> criteriaQuery = cb.createQuery(SalesByUserDto.class);
	    Root<Order> orderRoot = criteriaQuery.from(Order.class);

	    Predicate datePredicate = cb.between(orderRoot.get("date"), 
	            startDate.atStartOfDay(), 
	            endDate.atTime(LocalTime.MAX));
	    
	    Predicate statusPredicate = cb.notEqual(orderRoot.get("status"), OrderStatus.CANCELLED);
	    Predicate userPredicate = username != null ? cb.equal(orderRoot.get("user").get("username"), username) : cb.conjunction();

	    Expression<LocalDate> dateExpression = cb.function("DATE", LocalDate.class, orderRoot.get("date"));
	    
	    criteriaQuery.select(cb.construct(SalesByUserDto.class, 
	            orderRoot.get("user").get("username"),
	            dateExpression,
	            cb.toBigDecimal(cb.sum(orderRoot.get("totalPrice")))))
	        .where(cb.and(datePredicate, statusPredicate, userPredicate))
	        .groupBy(orderRoot.get("user").get("username"), dateExpression)
	        .orderBy(cb.asc(orderRoot.get("user").get("username")), cb.asc(dateExpression));

	    return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public List<SalesByProductDto> findSalesByProduct(LocalDate startDate, LocalDate endDate) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SalesByProductDto> cq = cb.createQuery(SalesByProductDto.class);
		Root<Order> order = cq.from(Order.class);
		Join<Order, OrderDetail> orderDetails = order.join("orderDetails");

		Predicate datePredicate = cb.between(order.get("date"), startDate, endDate);
		Predicate statusPredicate = cb.notEqual(order.get("status"), OrderStatus.CANCELLED);
		cq.select(cb.construct(SalesByProductDto.class, orderDetails.get("product").get("name"),
				cb.toBigDecimal(cb.sum(cb.prod(orderDetails.get("quantity"), orderDetails.get("price"))))))
				.where(cb.and(datePredicate, statusPredicate))
				.groupBy(orderDetails.get("product").get("name"));

		return entityManager.createQuery(cq).getResultList();
	}

	@Override
	public List<OrdersByStatusDto> findOrdersByStatus(LocalDate startDate, LocalDate endDate,OrderStatus status) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrdersByStatusDto> query = cb.createQuery(OrdersByStatusDto.class);
        Root<Order> order = query.from(Order.class);

        Predicate datePredicate = cb.between(order.get("date"), startDate, endDate);
        Predicate statusPredicate = status != null ? cb.equal(order.get("status"), status) : cb.notEqual(order.get("status"), OrderStatus.CANCELLED);

        query.select(cb.construct(OrdersByStatusDto.class,
                order.get("status"),
                cb.count(order)))
            .where(cb.and(datePredicate, statusPredicate))
            .groupBy(order.get("status"));

        return entityManager.createQuery(query).getResultList();
	}

}
