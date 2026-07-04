package com.example.backend.auth.controller;

import com.example.backend.common.dto.ApiResponse;
import com.example.backend.common.dto.Pagination;
import com.example.backend.common.security.custom.CustomUserDetails;
import com.example.backend.auth.dto.request.RoleUpdateRequest;
import com.example.backend.auth.dto.request.StatusUpdateRequest;
import com.example.backend.auth.dto.response.AccountResponse;
import com.example.backend.auth.dto.response.CurrentUserResponse;
import com.example.backend.auth.service.AccountManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("auth/accounts")
@RequiredArgsConstructor
public class AccountManagementController {
    private final AccountManagementService accountManagementService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CurrentUserResponse>> getCurrentUser(
            HttpServletRequest httpRequest
    ) {
        CurrentUserResponse response = accountManagementService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "User fetched successfully",
                httpRequest.getRequestURI()
        ));
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAdmins(
            HttpServletRequest httpRequest
    ) {
        List<AccountResponse> response = accountManagementService.getAllAdmins();
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Admins fetched successfully",
                httpRequest.getRequestURI()
        ));
    }

    @PatchMapping("/{accountId}/demote")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> demoteAdmin(
            @PathVariable UUID accountId,
            HttpServletRequest httpRequest
    ) {
        AccountResponse response = accountManagementService.demoteAdmin(accountId);
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Admin demoted successfully",
                httpRequest.getRequestURI()
        ));
    }

    @PatchMapping("/{accountId}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> updateStatus(
            @PathVariable UUID accountId,
            @Valid @RequestBody StatusUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        AccountResponse response = accountManagementService.updateStatus(accountId, request);
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Account status updated successfully",
                httpRequest.getRequestURI()
        ));
    }
}
