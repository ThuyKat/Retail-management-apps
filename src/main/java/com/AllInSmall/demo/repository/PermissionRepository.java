package com.AllInSmall.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AllInSmall.demo.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {


	Permission findByPermissionName(String s);

	
}
