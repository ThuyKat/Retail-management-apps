package com.AllInSmall.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AllInSmall.demo.model.Role;
import com.AllInSmall.demo.repository.RoleRepository;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;
	
	
	@GetMapping()
	public Iterable<Role>getAllRoles(){
		Iterable<Role>roles = roleRepository.findAll();
		return roles;
	}
}
