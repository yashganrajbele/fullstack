package com.example.backend.common.security.custom;

import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.auth.repository.AuthAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthAccountRepository authAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        AuthAccount authAccount = authAccountRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Auth account not found"));
        return new CustomUserDetails(authAccount);
    }

    public CustomUserDetails loadAuthAccountById(String authAccountId) throws UsernameNotFoundException {
        AuthAccount authAccount = authAccountRepository.findById(UUID.fromString(authAccountId))
                .orElseThrow(() -> new UsernameNotFoundException("Auth account not found"));
        return new CustomUserDetails(authAccount);
    }
}
