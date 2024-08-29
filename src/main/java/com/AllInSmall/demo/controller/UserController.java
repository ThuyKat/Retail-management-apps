package com.AllInSmall.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.AllInSmall.demo.dto.MyUserDetails;
import com.AllInSmall.demo.dto.UserRegistrationRequest;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.model.VerificationToken;
import com.AllInSmall.demo.repository.UserRepository;
import com.AllInSmall.demo.repository.VerificationTokenRepository;
import com.AllInSmall.demo.service.EmailService;
import com.AllInSmall.demo.service.GmailService;
import com.AllInSmall.demo.service.UserRegistrationService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/register")
	public String showRegistrationForm(Model model, Principal principal) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUD = (MyUserDetails) authentication.getPrincipal();
		String userRole = myUD.getRole().getRoleName();
		model.addAttribute("userRole", userRole);
		model.addAttribute("username", principal.getName());
		return "registerUser";
	}

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

	@GetMapping("/oauth2callback")
	public String handleOAuth2Callback(@RequestParam("code") String code, Model model, HttpSession session) {
		try {
			userRegistrationService.handleAuthorizationCallBack(code, session);
			return "registrationSuccess";
		} catch (Exception e) {
			model.addAttribute("error", "Failed to complete registration: " + e.getMessage());
			return "error";
		}
	}

	@GetMapping("/resetPassword")
	public ModelAndView changePassword(@RequestParam("token") String token) {
		// Find the token in the database
		VerificationToken dbToken = tokenRepository.findByToken(token);
		// Check if the token exists
		if (dbToken == null) {
			ModelAndView modelAndView = new ModelAndView("error");
			modelAndView.addObject("message", "Invalid token");
			return modelAndView;
		}
		// Check if token is expired
		if (dbToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			ModelAndView modelAndView = new ModelAndView("error");
			modelAndView.addObject("message", "Token has expired");
			return modelAndView;
		}

		// Token is valid and not expired, redirect to resetUsernamePassword page
		ModelAndView modelAndView = new ModelAndView("resetPassword");
		modelAndView.addObject("token", token);
		return modelAndView;
	}

	@PostMapping("/resetPassword")
	public ModelAndView updatePassword(@RequestParam("token") String token, @RequestParam("password") String password) {
		// Find the token in the database
		VerificationToken dbToken = tokenRepository.findByToken(token);
		ModelAndView modelAndView = null;
		// Check if the token exists
		if (dbToken == null) {
			modelAndView = new ModelAndView("error");
			modelAndView.addObject("message", "Invalid token");
			return modelAndView;
		}
		// Check if token is expired
		if (dbToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			modelAndView = new ModelAndView("error");
			modelAndView.addObject("message", "Token has expired");
			return modelAndView;
		}

		User user = dbToken.getUser();
		if (user == null) {
			modelAndView = new ModelAndView("error");
			modelAndView.addObject("message", "your account is not found");
			return modelAndView;
		}
		try {
			user.setPassword(password);
			userRepository.save(user);
			tokenRepository.delete(dbToken); // Remove the used token
			modelAndView = new ModelAndView("forgotPasswordMessage");
			modelAndView.addObject("message", "Thanks!!");
			return modelAndView;
		} catch (Exception e) {

			modelAndView = new ModelAndView("error");
			modelAndView.addObject("message", "error while updating password. Please try again!");
			return modelAndView;
		}

	}

}
