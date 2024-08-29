package com.AllInSmall.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class HierarchyConfig {

	@Bean
	public RoleHierarchy roleHierarchy() {

		return RoleHierarchyImpl.fromHierarchy("ROLE_Owner > ROLE_Manager > ROLE_Staff");
	}
}
