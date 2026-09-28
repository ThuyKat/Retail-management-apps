package com.AllInSmall.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.AllInSmall.demo.dto.UserDto;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.Permission;
import com.AllInSmall.demo.model.Role;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.repository.PermissionRepository;
import com.AllInSmall.demo.repository.RoleRepository;
import com.AllInSmall.demo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PermissionRepository permissionRepository;
	
	@Autowired
	@Qualifier("sessionOrder")
	Order sessionOrder;

	public List<UserDto> getAllUsers() {
		List<UserDto> showUsers = new ArrayList<>();
		List<User> allUsers = userRepository.findAll();
		User currentUser = sessionOrder.getUser();
		String currentUserRole = currentUser.getRole().getRoleName();
		for(User user : allUsers) {
			if("Manager".equalsIgnoreCase(currentUserRole)) {
				if("Staff".equalsIgnoreCase(user.getRole().getRoleName())) {
					UserDto userDto = new UserDto(user.getUsername(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getStatus(),user.getRole(),user.getLastLogin(),user.getCreatedDate());
					showUsers.add(userDto);
				}
			}else if("Owner".equalsIgnoreCase(currentUserRole)) {
				UserDto userDto = new UserDto(user.getUsername(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getStatus(),user.getRole(),user.getLastLogin(),user.getCreatedDate());
				showUsers.add(userDto);
			}
		}
		return showUsers;

	}

	public UserDto getUserByUsername(@PathVariable String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
		UserDto userDto = new UserDto(user.getUsername(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getStatus(),user.getRole(),user.getLastLogin(),user.getCreatedDate());

		return userDto;
	}

	public void updateUser(UserDto userDto) {
		log.info("username is: "+userDto.getUsername());
		User user = userRepository.findByUsername(userDto.getUsername())
	            .orElseThrow(() -> new EntityNotFoundException("User not found"));
		 if (userDto.getEmail() != null) {
	            user.setEmail(userDto.getEmail());
	        }
		 if (userDto.getStatus() != null) {
	            user.setStatus(userDto.getStatus());
	        }
		 if (userDto.getRole()!= null) {
	            Role role = roleRepository.findById(userDto.getRole().getId())
	                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
	            user.setRole(role);
	        }
		 if (userDto.getRole().getPermissions() != null) {
			 Set<Integer> permissionIds = userDto.getRole().getPermissions().stream()
				        .map(Permission::getId)
				        .collect(Collectors.toSet());
				    
			List<Permission> permissions = permissionRepository.findAllById(permissionIds);
		    user.getRole().setPermissions(Set.copyOf(permissions));
	        }
		  userRepository.save(user);

	}
}
