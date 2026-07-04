package com.example.backend.auth.controller;

import com.example.backend.common.dto.ApiResponse;
import com.example.backend.auth.dto.response.AdminPromotionRequestResponse;
import com.example.backend.auth.service.AdminPromotionRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("auth/admin-promotion-requests")
@RequiredArgsConstructor
public class AdminPromotionRequestController {
    private final AdminPromotionRequestService service;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<AdminPromotionRequestResponse>> createRequest(
            HttpServletRequest httpRequest
    ) {
        AdminPromotionRequestResponse response = service.createRequest();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                response,
                "Admin access request submitted successfully",
                httpRequest.getRequestURI()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<AdminPromotionRequestResponse>>> getAllRequests(
            HttpServletRequest httpRequest
    ) {
        List<AdminPromotionRequestResponse> response = service.getAllRequests();
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Admin promotion request fetched successfully",
                httpRequest.getRequestURI()
        ));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AdminPromotionRequestResponse>>> getPendingRequests(
            HttpServletRequest httpRequest
    ) {
        List<AdminPromotionRequestResponse> response = service.getPendingRequests();
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Pending requests fetched successfully",
                httpRequest.getRequestURI()
        ));
    }

    @PatchMapping("/{requestId}/approve")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminPromotionRequestResponse>> approveRequest(
            @PathVariable UUID requestId,
            HttpServletRequest httpRequest
    ) {
        AdminPromotionRequestResponse response = service.approveRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Request approved successfully",
                httpRequest.getRequestURI()
        ));
    }

    @PatchMapping("/{requestId}/reject")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminPromotionRequestResponse>> rejectRequest(
            @PathVariable UUID requestId,
            HttpServletRequest httpRequest
    ) {
        AdminPromotionRequestResponse response = service.rejectRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Request rejected successfully",
                httpRequest.getRequestURI()
        ));
    }
}
