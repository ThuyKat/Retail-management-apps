package com.AllInSmall.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AllInSmall.demo.dto.MyUserDetails;
import com.AllInSmall.demo.dto.UserDto;
import com.AllInSmall.demo.dto.UserRegistrationRequest;
import com.AllInSmall.demo.model.Permission;
import com.AllInSmall.demo.model.Role;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.model.VerificationToken;
import com.AllInSmall.demo.repository.PermissionRepository;
import com.AllInSmall.demo.repository.RoleRepository;
import com.AllInSmall.demo.repository.UserRepository;
import com.AllInSmall.demo.repository.VerificationTokenRepository;
import com.AllInSmall.demo.service.EmailService;
import com.AllInSmall.demo.service.GmailService;
import com.AllInSmall.demo.service.UserRegistrationService;
import com.AllInSmall.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Tag(name = "Users", description = " user management APIs")
@Slf4j
public class UserController {

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;

	@Operation(summary="Get form to register new user")
	@GetMapping("/register")
	public String showRegistrationForm(Model model, Principal principal) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUD = (MyUserDetails) authentication.getPrincipal();
		String userRole = myUD.getRole().getRoleName();
		model.addAttribute("userRole", userRole);
		model.addAttribute("username", principal.getName());
		return "registerUser";
	}

	@Operation(summary=" Register new user")
	@PostMapping("/register")
	public String registerUser(@ModelAttribute UserRegistrationRequest request,
			@RequestParam(name = "action", required = false) String action, HttpSession session, Model model) {

		String authUrl = null;
		try {
			authUrl = userRegistrationService.initiateRegistration(request, session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
		}
		if (authUrl.equalsIgnoreCase("noAuth")) {
			return "registrationSuccess";
		} else {
			model.addAttribute("authorizationUrl", authUrl);
			return "gmailAuthorization";
		}

	}

	@Operation(summary="Get all users")
	@GetMapping("/view")
	public String getAllUsers(Model model) {

		try {
			List<UserDto> users = userService.getAllUsers();
			model.addAttribute("users", users);
			return "showUserList";
		} catch (Exception e) {

			model.addAttribute("error", "unable to get all users " + e.getMessage());
			return "error";
		}

	}
	
	@Operation(summary="Get user by username")
	@GetMapping("/view/{username}")
	public String getUser(Model model,@PathVariable String username) {
		try {
		UserDto user = userService.getUserByUsername(username);
		List<Role>allRoles = roleRepository.findAll();
		List<Permission>allPermissions = permissionRepository.findAll();
		model.addAttribute("user",user);
		model.addAttribute("allPermissions", allPermissions);
		model.addAttribute("allRoles", allRoles);
		return "showUserDetails";
		}catch (Exception e) {

			model.addAttribute("error", "user not found " + e.getMessage());
			return "error";
		}

	}

	@GetMapping("/oauth2callback")
	public String handleOAuth2Callback(@RequestParam("code") String code, Model model, HttpSession session) {
		try {
			log.info("I am handling oauth2callback");
			userRegistrationService.handleAuthorizationCallBack(code, session);
			return "registrationSuccess";
		} catch (Exception e) {
			log.info(" error happens at oauth2callback");
			model.addAttribute("error", "Failed to complete registration: " + e.getMessage());
			return "error";
		}
	}

	
	
	@PostMapping("/update")
	public String updateUser( @ModelAttribute(name="user") UserDto userDto,RedirectAttributes redirectAttributes,Model model,HttpSession session) {
        try {
             userService.updateUser(userDto);
			redirectAttributes.addFlashAttribute("message", "User updated successfully!");
        } catch (Exception e) {
        	e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Failed to update user");
        }
        @SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
		navigationStack.pop(); //pop the current uri
		 session.setAttribute("navigationStack",navigationStack);
        return "redirect:"+navigationStack.peek();
        
    }

}
