package com.example.backend.common.security;

import com.example.backend.common.enums.AuthProvider;
import com.example.backend.common.enums.Role;
import com.example.backend.common.security.custom.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    public static UUID getAccountId() {
        return UUID.fromString(getCurrentUser().getAccountId());
    }

    public static String getUsername() {
        return getCurrentUser().getUsername();
    }

    public static Role getHighestRole() {
        return getCurrentUser().getHighestRole();
    }

    public static AuthProvider getAuthProvider() {
        return getCurrentUser().getAuthProvider();
    }
}
