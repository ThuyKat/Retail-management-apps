package com.AllInSmall.demo.dto;

import java.util.List;

import com.AllInSmall.demo.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListWrapper {

	private List<Product> products;
}
