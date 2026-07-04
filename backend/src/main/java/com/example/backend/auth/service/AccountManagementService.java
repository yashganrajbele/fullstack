package com.example.backend.auth.service;

import com.example.backend.auth.dto.request.StatusUpdateRequest;
import com.example.backend.auth.dto.response.AccountResponse;
import com.example.backend.auth.dto.response.CurrentUserResponse;

import java.util.List;
import java.util.UUID;

public interface AccountManagementService {
    CurrentUserResponse getCurrentUser();
    List<AccountResponse> getAllAdmins();
    AccountResponse demoteAdmin(UUID accountId);
    AccountResponse updateStatus(UUID accountId, StatusUpdateRequest request);
}
