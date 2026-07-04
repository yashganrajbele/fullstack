package com.example.backend.auth.service.helper;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateOtp() {
        return String.valueOf(
                100000 + RANDOM.nextInt(900000)
        );
    }
}