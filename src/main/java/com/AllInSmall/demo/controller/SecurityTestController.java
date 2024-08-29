package com.AllInSmall.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
public class SecurityTestController {

	@PreAuthorize("hasAuthority('read')")
	@GetMapping
	public String getOwnerData() {
		return "Owner data";
	}
	
//	@PreAuthorize("hasAnyRole('owner','manager)")
//	@GetMapping
//	public String getManagerData() {
//		return "Manager data";
//	}
//	
//	@PreAuthorize("hasAnyRole('owner','manager','staff')")
//	@GetMapping
//	public String getOwnerData() {
//		return "Owner data";
//	}
}
