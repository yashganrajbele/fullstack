package com.example.backend.auth.mapper;

import com.example.backend.auth.dto.response.AccountResponse;
import com.example.backend.auth.dto.response.AuthResponse;
import com.example.backend.auth.dto.response.CurrentUserResponse;
import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.common.security.custom.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public AuthResponse toResponse(String accessToken) {
        return new AuthResponse(accessToken);
    }

    public CurrentUserResponse toResponse(CustomUserDetails userDetails) {
        return new CurrentUserResponse(
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getHighestRole(),
                userDetails.getAuthProvider(),
                userDetails.isEmailVerified()
        );
    }

    public AccountResponse toResponse(AuthAccount authAccount) {
        return new AccountResponse(
                authAccount.getId(),
                authAccount.getUsername(),
                authAccount.getEmail(),
                authAccount.getRoles(),
                authAccount.isEnabled(),
                authAccount.isAccountNonLocked(),
                authAccount.isEmailVerified()
        );
    }
}
