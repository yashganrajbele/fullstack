package com.example.backend.auth.service.impl;

import com.example.backend.auth.dto.response.GoogleUserInfo;
import com.example.backend.auth.service.GoogleIdentityService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleIdentityServiceImpl implements GoogleIdentityService {
    private final JwtDecoder googleJwtDecoder;
    @Value("${google.client-id}")
    private String googleClientId;

    @Override
    public GoogleUserInfo verify(String idToken) {
        Jwt jwt = googleJwtDecoder.decode(idToken);

        String audience = jwt.getAudience().getFirst();
        if (!googleClientId.equals(audience)) {
            throw new IllegalArgumentException(
                    "Invalid Google token audience"
            );
        }

        String providerId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        Boolean emailVerified = jwt.getClaim("email_verified");
        return new GoogleUserInfo(providerId, email, name, Boolean.TRUE.equals(emailVerified));
    }
}
