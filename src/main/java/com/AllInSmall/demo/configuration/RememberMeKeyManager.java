package com.AllInSmall.demo.configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class RememberMeKeyManager {

	private Map<String, KeyData> validKeys; // map of key:KeyId, vaue: Key
    private String currentKeyId;  //this is the one browser sends
    private String oldKeyId; // this is to save the one browser sends while still generate new keys at background when time comes
   
    public RememberMeKeyManager() {
        this.validKeys = new ConcurrentHashMap<>();
        this.currentKeyId = null;
        this.oldKeyId = null;
        rotateKey(); // Initialize with a key
    }
    public void rotateKey() {
    	String newKeyId = generateKeyId();
        String newKey = generateNewKey();
     // Store the current key as old key
        if (currentKeyId != null) {
            oldKeyId = currentKeyId;
        }
     // Set the new key as current
        validKeys.put(newKeyId, new KeyData(newKey));
        currentKeyId = newKeyId;
    }
    
    public String getCurrentKey() {
        return validKeys.get(currentKeyId).getKey();
    }
    public String getOldKey() {
        return oldKeyId != null ? validKeys.get(oldKeyId).getKey() : null;
    }
  
    
    public void removeOldKey() {
        if (oldKeyId != null) {
            validKeys.remove(oldKeyId);
            oldKeyId = null;
        }
    }
    private String generateKeyId() {
        return UUID.randomUUID().toString();
    }
    private String generateNewKey() {
        // Implement secure key generation
        return UUID.randomUUID().toString();
    }
    
    public boolean shouldRotateKey() {
        KeyData currentKeyData = validKeys.get(currentKeyId);
        if (currentKeyData == null) {
            return true; // If there's no current key, we should definitely rotate
        }
        
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime keyCreationTime = currentKeyData.getCreationTime().withZoneSameLocal(ZoneId.systemDefault());
        
        // Check if it's been more than 2 weeks since the key was created
        return ChronoUnit.WEEKS.between(keyCreationTime, now) >= 2;
    }
    private static class KeyData {
        private final String key;
        private final ZonedDateTime creationTime;
        
        public KeyData(String key) {
            this.key = key;
            this.creationTime = ZonedDateTime.now();
        }
        
        public String getKey() {
            return key;
        }
        
        public ZonedDateTime getCreationTime() {
            return creationTime;
        }
    }
    
}
