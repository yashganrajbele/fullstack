package com.example.backend.auth.dto.response;

import com.example.backend.common.enums.AuthProvider;
import com.example.backend.common.enums.Role;

public record CurrentUserResponse(
        String username,
        String email,
        Role highestRole,
        AuthProvider provider,
        boolean emailVerified
) {
}
