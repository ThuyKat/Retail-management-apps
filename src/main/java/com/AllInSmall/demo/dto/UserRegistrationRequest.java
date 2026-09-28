package com.AllInSmall.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegistrationRequest {
	@NotBlank(message = "this field is mandatory")
	 String firstName;
	@NotBlank(message = "this field is mandatory")
	 String lastName;
	 
	 @NotBlank(message = "this field is mandatory")
	 String email;
	 @NotBlank(message = "this field is mandatory")
	 String role;
	 @NotBlank(message = "this field is mandatory")
	 String mobile;
	 @NotBlank(message = "this field is mandatory")
	 String permissions;
	
	
}
