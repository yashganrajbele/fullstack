package com.example.backend.common.event.events;

import java.time.LocalDateTime;

public record AdminAccessRevokedEvent(
        String email,
        String username,
        String revokedBy,
        LocalDateTime revokedAt
) {
}
