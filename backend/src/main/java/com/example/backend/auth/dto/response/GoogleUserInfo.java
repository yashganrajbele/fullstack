package com.example.backend.auth.dto.response;

public record GoogleUserInfo(
        String providerId,
        String email,
        String name,
        boolean emailVerified
) {
}
