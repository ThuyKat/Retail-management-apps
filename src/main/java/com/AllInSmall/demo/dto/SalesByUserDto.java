package com.AllInSmall.demo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesByUserDto implements Serializable {
	//DTO constructor must match the order in which they are selected in query
    private static final long serialVersionUID = 1L;
    String username;
    LocalDate date;
    BigDecimal totalSales;
}