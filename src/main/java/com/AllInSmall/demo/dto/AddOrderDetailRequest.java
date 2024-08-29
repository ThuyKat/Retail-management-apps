package com.AllInSmall.demo.dto;



import com.AllInSmall.demo.model.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

//@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddOrderDetailRequest {

	Product product;
	
	int quantity;
	
	
}
