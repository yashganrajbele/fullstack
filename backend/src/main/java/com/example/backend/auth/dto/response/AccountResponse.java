package com.example.backend.auth.dto.response;

import com.example.backend.common.enums.Role;

import java.util.Set;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String username,
        String email,
        Set<Role> roles,
        boolean enabled,
        boolean accountNonLocked,
        boolean emailVerified
) {
}
