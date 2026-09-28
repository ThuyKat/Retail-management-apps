package com.AllInSmall.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesByProductDto implements Serializable {

	private static final long serialVersionUID = 1L;
	String productName;
	BigDecimal totalSales;
}
