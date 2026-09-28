package com.AllInSmall.demo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

	Set<OrderDetail> findByOrderId(int orderId);

	Set<OrderDetail> findByProductId(Integer id);

	List<OrderDetail> findBySizeId(Integer id);

}
