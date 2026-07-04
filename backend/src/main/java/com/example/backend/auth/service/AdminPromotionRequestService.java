package com.example.backend.auth.service;

import com.example.backend.common.enums.RequestStatus;
import com.example.backend.auth.dto.response.AdminPromotionRequestResponse;
import com.example.backend.auth.entity.AdminPromotionRequest;

import java.util.List;
import java.util.UUID;

public interface AdminPromotionRequestService {
    AdminPromotionRequestResponse createRequest();
    List<AdminPromotionRequestResponse> getAllRequests();
    List<AdminPromotionRequestResponse> getPendingRequests();
    AdminPromotionRequestResponse approveRequest(UUID requestId);
    AdminPromotionRequestResponse rejectRequest(UUID requestId);
}
