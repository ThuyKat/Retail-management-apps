package com.AllInSmall.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

	List<Category> findByParentIsNull();

	List<Category> findByParentId(int parentId);

	boolean existsByNameIgnoreCase(String name);
	
	

}
