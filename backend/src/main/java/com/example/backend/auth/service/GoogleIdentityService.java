package com.example.backend.auth.service;

import com.example.backend.auth.dto.response.GoogleUserInfo;

public interface GoogleIdentityService {
    GoogleUserInfo verify(String idToken);
}
