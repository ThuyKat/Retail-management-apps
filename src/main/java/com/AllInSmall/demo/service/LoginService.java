package com.AllInSmall.demo.service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.AllInSmall.demo.dto.GenericResponse;
import com.AllInSmall.demo.model.User;
import com.AllInSmall.demo.model.VerificationToken;
import com.AllInSmall.demo.repository.UserRepository;
import com.AllInSmall.demo.repository.VerificationTokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {
	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Environment env;
	
	 @Autowired
	private MessageSource messages;
	 
	 @Autowired
	 private JavaMailSender mailSender;
	 
	 

	public GenericResponse resetPassword(String email,HttpServletRequest request) {
		try {
		// Generate a verification token and save it to DB
		VerificationToken token = null;
		User user = userRepository.findByEmail(email);
		log.info("finding user by email"+email);
		if(user !=null) {
			log.info("found user");
			token = tokenRepository.findByUserId(user.getId());
			if(token == null|| token.getExpiryDate().isBefore(LocalDateTime.now()) ){
				log.info("no token is found, generating new one");
			String newTokenString = UUID.randomUUID().toString();
			token = new VerificationToken(newTokenString, user);
			tokenRepository.save(token);
			}
			try {
				  System.out.println("Attempting to send email to: " + user.getEmail());
            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token.getToken(), user));
            System.out.println("Email sent successfully");

			}catch(Exception e) {
				e.printStackTrace();
			}

			}

		
        //The message will be whatever string is defined for the key "message.resetPasswordEmail" in localized messages file
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
		}catch(Exception e) {
			e.printStackTrace();
			return new GenericResponse("error");
		}
	}
		//
	    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
	    	 final String url = contextPath + "/user/resetPassword?token=" + token;
	         final String message = messages.getMessage("message.resetPassword", null, locale);
	         return constructEmail("Reset Password", message + " \r\n" + url, user);
	    }
	    
	    private SimpleMailMessage constructEmail(String subject, String body, User user) {
	        final SimpleMailMessage email = new SimpleMailMessage();
	        email.setSubject(subject);
	        email.setText(body);
	        email.setTo(user.getEmail());
	        email.setFrom(env.getProperty("support.email"));
	        return email;
	    }
	    
	    private String getAppUrl(HttpServletRequest request) {
	        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	    }

		
		

	}


