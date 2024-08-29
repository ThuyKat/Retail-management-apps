package com.AllInSmall.demo.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.AllInSmall.demo.model.GmailServiceCredential;
import com.AllInSmall.demo.repository.GmailCredentialRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;


@Service
public class GmailService {
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
	private static final NetHttpTransport HTTP_TRANSPORT;
	/*
	 * static block: initialization of a class,done only once, setting up static
	 * fields or resources that are shared across all instances of the class.
	 * one-time setup tasks at the class level different to Autowired: injecting
	 * dependencies into Spring-managed beans at the instance level.
	 */
	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException("Failed to create HTTP transport", e);
		}
	}
	@Autowired
	GmailCredentialRepository gmailRepo;

	@Value("${google.client.id}")
	private String clientId;

	@Value("${google.client.secret}")
	private String clientSecret;

	@Value("${google.redirect.uri}") // this is the server open to get the Gmail response from Google
	private String redirectUri;

	public Gmail getGmailService() throws IOException, GeneralSecurityException {
		Gmail service = null;

		//  get the refresh token from db
		List<GmailServiceCredential> encryptedTokens = gmailRepo.findAll();
		if (!encryptedTokens.isEmpty()) {
			// Decrypt the token
			String refreshToken = new String(Base64.getDecoder().decode(encryptedTokens.get(0).getDescription()));

			// Create and return a new Credential object
			Credential credential = new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
					.setJsonFactory(GsonFactory.getDefaultInstance()).setClientSecrets(clientId, clientSecret).build()
					.setRefreshToken(refreshToken);
			service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("AIS").build();
			return service;
		} 
		return service;
	}

	public String getAuthorizationUrl() throws IOException {
		GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
				.setInstalled(new GoogleClientSecrets.Details().setClientId(clientId).setClientSecret(clientSecret));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setAccessType("offline").build();
// Generate the authorization URL
		String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri).build();
		return authorizationUrl;
	}

	public void handleAuthorizationCode(String code) throws IOException {

		GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
				.setInstalled(new GoogleClientSecrets.Details().setClientId(clientId).setClientSecret(clientSecret));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setAccessType("offline").build();

		TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();

		Credential credential = flow.createAndStoreCredential(response, "user");
		storeCredential(credential); // Implement this method to store the credential securely

	}

	private void storeCredential(Credential credential) {
		String refreshToken = credential.getRefreshToken();
		String encryptedToken = Base64.getEncoder().encodeToString(refreshToken.getBytes());
		GmailServiceCredential dbCredential = new GmailServiceCredential();
		dbCredential.setDescription(encryptedToken);
		gmailRepo.deleteAll();
		gmailRepo.save(dbCredential);

	}
}
