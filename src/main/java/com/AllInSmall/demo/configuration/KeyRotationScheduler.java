package com.AllInSmall.demo.configuration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KeyRotationScheduler {

	private final RememberMeKeyManager keyManager;

    public KeyRotationScheduler(RememberMeKeyManager keyManager) {
        this.keyManager = keyManager;
    }

    @Scheduled(fixedRate = 86400000) // Check daily
    public void checkAndRotateKey() {
        if (keyManager.shouldRotateKey()) {
            keyManager.rotateKey();
        }
    }
}
