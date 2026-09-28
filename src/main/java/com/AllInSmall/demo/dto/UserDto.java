package com.AllInSmall.demo.dto;

import java.time.LocalDateTime;


import com.AllInSmall.demo.enums.UserStatus;
import com.AllInSmall.demo.model.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
	
	
	String username;
	String firstName;
	String lastName;
	String email;
	UserStatus status;
	Role role;
	LocalDateTime lastLogin;
	LocalDateTime createdDate;

}
