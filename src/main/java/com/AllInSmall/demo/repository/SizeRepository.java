package com.AllInSmall.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AllInSmall.demo.model.Size;

public interface SizeRepository extends JpaRepository<Size, Integer> {

	 List<Size> findByCategoryId(int categoryId);
}
