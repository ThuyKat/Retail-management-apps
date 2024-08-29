package com.AllInSmall.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.AllInSmall.demo.dto.UserRegistrationRequest;
import com.AllInSmall.demo.enums.UserStatus;
import com.AllInSmall.demo.model.Permission;
import com.AllInSmall.demo.model.Role;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.model.VerificationToken;
import com.AllInSmall.demo.repository.PermissionRepository;
import com.AllInSmall.demo.repository.RoleRepository;
import com.AllInSmall.demo.repository.UserRepository;
import com.AllInSmall.demo.repository.VerificationTokenRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserRegistrationService {
	
	private UserRepository userRepository;
		
	private PermissionRepository permissionRepository;
	
	private RoleRepository roleRepository;
	
	private EmailService emailService;
	
//	private PasswordEncoder passwordEncoder;
	
	private VerificationTokenRepository tokenRepository;

	@Autowired
	public UserRegistrationService(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService,
			VerificationTokenRepository tokenRepository,PermissionRepository permissionRepository,RoleRepository roleRepository1) {
		super();
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.tokenRepository = tokenRepository;
		this.permissionRepository = permissionRepository;
		this.roleRepository = roleRepository1;
	}
	
	
	public String initiateRegistration(UserRegistrationRequest request,HttpSession session) throws Exception {
        Set<Permission> permissions = Arrays.stream(request.getPermissions().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s ->permissionRepository.findByPermissionName(s))
                .collect(Collectors.toSet());
        Role role = roleRepository.findByRoleName(request.getRole());
        role.setPermissions(permissions);
		
		User user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.mobile(request.getMobile())
				.role(role)
				.status(UserStatus.INACTIVE)
				.build();
		userRepository.save(user);
		
		//Generate a verification token
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken(token,user);
		tokenRepository.save(verificationToken);
		// Store registration details in session
        session.setAttribute("pendingUser", user);
        session.setAttribute("pendingToken", token);
		//send email with verification link
		String authUrl = emailService.sendRegistrationEmail(user, token);
		return authUrl;
		
	}


	public void finaliseRegistration(User user, String username, String password) {
		user.setUsername(username);
//		user.setPassword(passwordEncoder.encode(password));
		user.setPassword(password);
		user.setStatus(UserStatus.ACTIVE); //enable the user after setting password
		
		userRepository.save(user);
		
		
	}


	public void handleAuthorizationCallBack(String code, HttpSession session) throws Exception {
		emailService.handleAuthorizationCode(code);
		// Retrieve stored registration details
        User user = (User) session.getAttribute("pendingUser");
        String token = (String) session.getAttribute("pendingToken");
        // Clear session attributes
        session.removeAttribute("pendingUser");
        session.removeAttribute("pendingToken");
        // Attempt to send email again
        emailService.sendRegistrationEmail(user, token);
		
	}
	
	
	
	

}
