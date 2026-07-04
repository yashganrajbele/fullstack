package com.example.backend.common.event.events;

import com.example.backend.common.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestStatusUpdatedEvent(
        UUID requestId,
        String username,
        String email,
        RequestStatus status,
        LocalDateTime reviewedAt,
        String reviewedBy
) {
}
