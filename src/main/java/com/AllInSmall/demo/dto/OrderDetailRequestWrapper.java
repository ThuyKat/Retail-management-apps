package com.AllInSmall.demo.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestWrapper {
	private List<AddOrderDetailRequest> orderItems ;
	

}
