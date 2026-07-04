package com.example.backend.auth.dto.request;

import com.example.backend.common.enums.Role;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequest(
        @NotNull Role role
) {
}
