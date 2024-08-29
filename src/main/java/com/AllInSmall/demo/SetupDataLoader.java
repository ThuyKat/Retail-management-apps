package com.AllInSmall.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.AllInSmall.demo.enums.UserStatus;
import com.AllInSmall.demo.model.Permission;
import com.AllInSmall.demo.model.Role;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.repository.PermissionRepository;
import com.AllInSmall.demo.repository.RoleRepository;
import com.AllInSmall.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	/*
	 * This class is for setup logic: creating default users, roles, privileges if they dont exist
	 * This can also be used to populate the database with initial data
	 * Any other one-time setup tasks that need to occur when the application starts
	 */
	
	boolean alreadySetup = false;
	private RoleRepository roleRepository;
	private PermissionRepository permissionRepository;
	private UserRepository userRepository;
	
	@Autowired
	public SetupDataLoader(RoleRepository roleRepository,PermissionRepository permissionRepository,UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) { //each time the application refreshed, run this
		
		log.info("I am in setup data loader class");
		if(alreadySetup) {
			return;
		}
		Permission processOrder = createPermissionIfNotFound("ORDER_PROCESS");
		Permission placeOrder = createPermissionIfNotFound("ORDER_PLACE");
		Permission registerUser = createPermissionIfNotFound("USER_REGISTER");
		Permission approveCancelOrder = createPermissionIfNotFound("ORDER_CANCEL_APPROVE");
		Permission viewReport = createPermissionIfNotFound("REPORT_VIEW");
		Permission manageCategory = createPermissionIfNotFound("CATEGORY_MANAGE");
		Permission manageProduct = createPermissionIfNotFound("PRODUCT_MANAGE");
		Set<Permission> staffPermissions = new HashSet<>(Arrays.asList(processOrder, placeOrder)); 
		//using Set.of(..) will results in immutable set: not able to add, remove,edit the set
		Set<Permission> managerPermissions = new HashSet<>(Arrays.asList(registerUser, approveCancelOrder,viewReport)); 
		Set<Permission> ownerPermissions = new HashSet<>(Arrays.asList(manageCategory, manageProduct)); 
		createRoleIfNotFound("Staff",staffPermissions);
		createRoleIfNotFound("Manager",managerPermissions);
		createRoleIfNotFound("Owner",ownerPermissions);
		
		// Create default user
		Role defaultRole = createRoleIfNotFound("Owner",ownerPermissions);
        createDefaultUserIfNotFound("thuy", "pass", defaultRole);
		alreadySetup = true;
		
	}
	private void createDefaultUserIfNotFound(String username, String password, Role defaultRole) {
		
		Optional<User> userOptional = userRepository.findByUsername(username);
		if(userOptional.isEmpty()) {
		User user = new User();
		user.setPassword(password);
		user.setUsername(username);
		user.setRole(defaultRole);
		user.setStatus(UserStatus.ACTIVE);
		userRepository.save(user);
		log.info("default user is saved ");
		}
		
	}

	@Transactional
	private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
		Role role = roleRepository.findByRoleName(name);
		if(role == null) {
			role = new Role();
			role.setRoleName(name);
			role.setPermissions(permissions);
			roleRepository.save(role);
			log.info("new role is saved to database: " + role.getRoleName());
		}
		return role;
	
	}
	@Transactional
	private Permission createPermissionIfNotFound(String name) {
		// TODO Auto-generated method stub
		Permission permission = permissionRepository.findByPermissionName(name);
		if(permission == null) {
			permission = new Permission();
			permission.setPermissionName(name);
			permissionRepository.save(permission);
			log.info("new permission is saved to database: " + permission.getPermissionName());
			
		}
		return permission;
	}
	
	

}
