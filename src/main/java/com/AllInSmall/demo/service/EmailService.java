package com.AllInSmall.demo.service;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.AllInSmall.demo.model.User;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private GmailService gmailService;

	@Value("${app.host}")
	private String appHost;

	public String sendRegistrationEmail(User user, String token) throws Exception {
		
		String url = appHost + "/register/confirmRegistration?token=" + token;
		String subject = "Complete your registration";
		String message = "Please click the following link to complete your registration: " + url;
//		SimpleMailMessage email = new SimpleMailMessage();
//		email.setFrom("noreply@ais.com");
//		email.setTo(user.getEmail());
//		email.setSubject("Complete Your Registration");
//		email.setText(message);
//		
//		mailSender.send(email);
		Gmail service = gmailService.getGmailService();
		if (service != null) {
			MimeMessage mimeMessage = createEmail(user.getEmail(), "noreply@ais.com", subject, message);
			String userId = "me"; // This refers to myself - the authenticated user
			sendMessage(service, userId, mimeMessage);
			return "noAuth";
		}else {
			String authUrl = gmailService.getAuthorizationUrl();
			return authUrl;
		}
			
	}

	private void sendMessage(Gmail service, String userId, MimeMessage mimeMessage)
			throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		mimeMessage.writeTo(buffer);
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
		Message message = new Message();
		message.setRaw(encodedEmail);
		service.users().messages().send(userId, message).execute();

	}

	private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(from));
		email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	public void handleAuthorizationCode(String code) throws IOException {
		gmailService.handleAuthorizationCode(code);
		
	}

	
}
