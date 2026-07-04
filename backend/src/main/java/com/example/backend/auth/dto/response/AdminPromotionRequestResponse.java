package com.example.backend.auth.dto.response;

import com.example.backend.common.enums.RequestStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AdminPromotionRequestResponse(
        UUID requestId,
        UUID accountId,
        String username,
        RequestStatus status,
        String reviewedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}