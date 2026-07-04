package com.example.backend.auth.dto.request;

public record StatusUpdateRequest(
        Boolean enabled,
        Boolean accountNonLocked
) {
}
