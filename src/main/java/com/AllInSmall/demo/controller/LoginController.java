package com.AllInSmall.demo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.model.VerificationToken;
import com.AllInSmall.demo.repository.VerificationTokenRepository;
import com.AllInSmall.demo.service.LoginService;
import com.AllInSmall.demo.service.UserRegistrationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class LoginController {

//	@Autowired
//	private LoginService loginService;
	@Autowired
	private VerificationTokenRepository tokenRepository;
	@Autowired
	private UserRegistrationService userRegistrationService;
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("/login")
	public String showLogin() {
		log.info("I am in login controller");
		return "login";
	}

	@GetMapping("/forgotPassword")
	public String showForgotPasswordForm() {
		System.out.println(LocaleContextHolder.getLocale());
	    return "forgotPasswordForm";
	}
	
	@GetMapping("/register/confirmRegistration")
	public ModelAndView confirmRegistration(@RequestParam("token") String token) {
		// Find the token in the database
		VerificationToken dbToken = tokenRepository.findByToken(token);
		
		System.out.println("reached the confirmRegistration in userController,token found:  " +dbToken);
		
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
		ModelAndView modelAndView = new ModelAndView("resetUsernamePassword");
		modelAndView.addObject("token", token);
		return modelAndView;
	}
	@PostMapping("/register/finaliseRegistration")
	public ResponseEntity<String> finaliseRegistration(@ModelAttribute("token") String token,
			@RequestParam("username") String username, @RequestParam("password") String password) {
		System.out.println(" I am at finalise registration in user controller: " + token);
		VerificationToken dbToken = tokenRepository.findByToken(token);
		if (dbToken == null) {
			return ResponseEntity.badRequest().body("Invalid token");
		}
		
		if (dbToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			return ResponseEntity.badRequest().body("Token has expired");
		}

		User user = dbToken.getUser();
		if (user == null) {
			return ResponseEntity.badRequest().body("User not found");
		}
		try {
			userRegistrationService.finaliseRegistration(user, username, password);
			tokenRepository.delete(dbToken); // Remove the used token
			return ResponseEntity.ok("Registration completed successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error finalising registration: " + e.getMessage());
		}
	}
	
	@PostMapping("/forgotPassword")
	public String processForgotPassword(@RequestParam("email") String email, Model model,HttpServletRequest request) {
		loginService.resetPassword(email, request);
		return "forgotPasswordMessage";
	}
	
	@GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied"; 
    }

//	@PostMapping("/login")
//	public String doLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
//		// Authenticate via LoginService
//		User user = loginService.authenticate(email, password);
//		if (user != null) {
//			model.addAttribute("username", user.getFirstName());
//			return "redirect:/";
//		} else {
//			model.addAttribute("error", "Invalid email or password");
//			return "login";
//		}
//	}
	
//	@GetMapping("/logout")
//	public String logout() {
//	    return "redirect:/login";
//	}

}
