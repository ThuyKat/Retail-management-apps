package com.AllInSmall.demo.configuration;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.AllInSmall.demo.dto.MyUserDetails;
import com.AllInSmall.demo.model.Order;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	@Qualifier("sessionOrder")
	Order sessionOrder;
	
	@Autowired
	UserRepository userRepository;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("I am in custom authentication handler");
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUD = (MyUserDetails) authentication.getPrincipal();
//		Set<String> permission = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		String userRole = myUD.getRole().getRoleName();
		String username = myUD.getUsername();
		Optional<User> userOptional = userRepository.findByUsername(username);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			sessionOrder.setUser(user);
		}
		
//		if (roles.contains("owner")) {
		if("Owner".equals(userRole) || "Manager".equals(userRole)) {
			response.sendRedirect("/management");
		} else {
			response.sendRedirect("/index");
		}
	}
}
