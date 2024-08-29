package com.AllInSmall.demo.repository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.AllInSmall.demo.model.PersistentLogin;

@Component
public class MyPersistentTokenRepositoryImpl implements PersistentTokenRepository {

	/*
	 * PersistentTokenRepository is defined by Spring security, include methods
	 * specifically designed for remember-me functionality createNewToken(),
	 * updateToken(), getTokenForSeries(), and removeUserTokens()
	 */
	
	private RememberMeTokenRepository myTokenRepository;
	private RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256; // this is enum
	private RememberMeTokenAlgorithm matchingAlgorithm = RememberMeTokenAlgorithm.MD5;
	
	@Autowired
	 public MyPersistentTokenRepositoryImpl(RememberMeTokenRepository myTokenRepository) {
		 this.myTokenRepository = myTokenRepository;
	 }
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		PersistentLogin persistentLogin = new PersistentLogin();
		persistentLogin.setSeries(token.getSeries());
		persistentLogin.setUsername(token.getUsername());
		persistentLogin.setToken(encodeToken(token.getTokenValue(),encodingAlgorithm));
		persistentLogin.setLastUsed(token.getDate());
		myTokenRepository.save(persistentLogin);

	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		PersistentLogin persistentLogin = myTokenRepository.findById(series).orElse(null);
		if (persistentLogin != null) {
			persistentLogin.setToken(encodeToken(tokenValue,encodingAlgorithm));
			persistentLogin.setLastUsed(lastUsed);
			myTokenRepository.save(persistentLogin);
		}
	}

	private String encodeToken(String token, RememberMeTokenAlgorithm algorithm) {

		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm.name());
			byte[] encodedHash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
			return new String(Hex.encode(encodedHash));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Failed to encode token", e);
		}
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		PersistentLogin persistentLogin = myTokenRepository.findById(seriesId).orElse(null);
		return persistentLogin != null
				? new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
						persistentLogin.getToken(), // Note: This is the encoded token
						persistentLogin.getLastUsed())
				: null;
	}

	@Override
	@Transactional //it causes error if not adding Transactional annotation
	public void removeUserTokens(String username) {
		myTokenRepository.deleteByUsername(username);

	}
	
	 public boolean matchesToken(String presentedToken, String series) {
		    PersistentLogin persistentLogin = myTokenRepository.findById(series).orElse(null);
		    if (persistentLogin != null) {
		        String storedToken = persistentLogin.getToken();
		        return storedToken.equals(encodeToken(presentedToken,encodingAlgorithm)) ||
		               storedToken.equals(encodeToken(presentedToken,matchingAlgorithm));
		    }
		    return false;

	    }

	public void setEncodingAlgorithm(RememberMeTokenAlgorithm encodingAlgorithm) {
		this.encodingAlgorithm = encodingAlgorithm;
	}

	public void setMatchingAlgorithm(RememberMeTokenAlgorithm matchingAlgorithm) {
		this.matchingAlgorithm = matchingAlgorithm;
	}
	
//	private String generateTokenSignature(long tokenExpiryTime, String username, String password, String currentKey) {
//		// TODO Auto-generated method stub
//		String data = username + ":" + tokenExpiryTime + ":" + password + ":" + currentKey;
//	    MessageDigest digest;
//	    try {
//	        digest = MessageDigest.getInstance("MD5");
//	    } catch (NoSuchAlgorithmException e) {
//	        throw new IllegalStateException("No MD5 algorithm available!");
//	    }
//	    return new String(Hex.encode(digest.digest(data.getBytes())));
//
//	}

}
