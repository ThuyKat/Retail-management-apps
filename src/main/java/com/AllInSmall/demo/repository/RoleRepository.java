package com.AllInSmall.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AllInSmall.demo.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {


	Role findByRoleName(String role);

}
