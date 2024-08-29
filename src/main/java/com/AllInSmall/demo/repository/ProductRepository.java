package com.AllInSmall.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.AllInSmall.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

	List<Product> findProductByCategoryId(Integer categoryId);
	Optional<Product>findById(int productId); //Jpa convention to use Optional with methods such as .isPresent(), .orElse(null)
	
}
	