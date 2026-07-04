package com.example.backend.auth.service.helper;

import com.example.backend.auth.repository.AuthAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernameGenerator {
    private final AuthAccountRepository authAccountRepository;
    public String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0].toLowerCase();
        String username = baseUsername;
        int counter = 1;
        while (authAccountRepository.existsByUsername(username)) {
            username = baseUsername + "_" + counter++;
        }
        return username;
    }
}
