package com.example.backend.auth.mapper;

import com.example.backend.auth.dto.response.AdminPromotionRequestResponse;
import com.example.backend.auth.entity.AdminPromotionRequest;
import org.springframework.stereotype.Component;

@Component
public class AdminPromotionRequestMapper {
    public AdminPromotionRequestResponse toResponse(AdminPromotionRequest adminPromotionRequest) {
        return new AdminPromotionRequestResponse(
                adminPromotionRequest.getId(),
                adminPromotionRequest.getAccountId(),
                adminPromotionRequest.getUsername(),
                adminPromotionRequest.getStatus(),
                adminPromotionRequest.getReviewedBy(),
                adminPromotionRequest.getCreatedAt(),
                adminPromotionRequest.getUpdatedAt()
        );
    }
}
