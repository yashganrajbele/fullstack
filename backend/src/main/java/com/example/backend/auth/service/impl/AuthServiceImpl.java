package com.example.backend.auth.service.impl;

import com.example.backend.common.enums.AuthProvider;
import com.example.backend.common.event.EventPublisher;
import com.example.backend.common.event.events.AccountCreatedEvent;
import com.example.backend.common.exception.BaseException;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.common.security.SecurityUtils;
import com.example.backend.auth.dto.request.ChangePasswordRequest;
import com.example.backend.auth.dto.request.GoogleLoginRequest;
import com.example.backend.auth.dto.request.LoginRequest;
import com.example.backend.auth.dto.request.RegisterRequest;
import com.example.backend.auth.dto.response.AuthResponse;
import com.example.backend.auth.dto.response.GoogleUserInfo;
import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.auth.mapper.AuthMapper;
import com.example.backend.auth.repository.AuthAccountRepository;
import com.example.backend.common.security.custom.CustomUserDetails;
import com.example.backend.common.security.jwt.JwtService;
import com.example.backend.auth.service.AuthService;
import com.example.backend.auth.service.GoogleIdentityService;
import com.example.backend.auth.service.helper.UsernameGenerator;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthAccountRepository authAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final GoogleIdentityService googleIdentityService;
    private final UsernameGenerator usernameGenerator;
    private final EmailService emailService;
    private final EventPublisher eventPublisher;

    @Override
    public AuthResponse register(RegisterRequest request) {
        AuthAccount account = AuthAccount.builder()
                .username(usernameGenerator.generateUniqueUsername(request.email()))
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        AuthAccount savedAccount = authAccountRepository.save(account);
//        eventPublisher.publish(new AccountCreatedEvent(
//                savedAccount.getEmail(),
//                savedAccount.getUsername()
//        ));
        CustomUserDetails userDetails = new CustomUserDetails(savedAccount);
        String token = jwtService.generateToken(userDetails);
        return authMapper.toResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(), request.password()
                ));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return authMapper.toResponse(token);
    }

    @Override
    public AuthResponse googleLogin(GoogleLoginRequest request) {
        GoogleUserInfo googleUser = googleIdentityService.verify(request.idToken());
        AuthAccount account = authAccountRepository.findByProviderAndProviderId(
                AuthProvider.GOOGLE, googleUser.providerId()
        ).orElseGet(() -> createGoogleUser(googleUser));
        CustomUserDetails userDetails = new CustomUserDetails(account);
        String accessToken = jwtService.generateToken(userDetails);
        return new AuthResponse(accessToken);
    }

    public AuthAccount createGoogleUser(GoogleUserInfo googleUser) {
        Optional<AuthAccount> existingEmailUser = authAccountRepository.findByEmail(
                googleUser.email()
        );
        if (existingEmailUser.isPresent()) {
            AuthAccount account = existingEmailUser.get();
            account.setProvider(AuthProvider.GOOGLE);
            account.setProviderId(googleUser.providerId());
            account.setEmailVerified(true);
            return authAccountRepository.save(account);
        }
        AuthAccount account = AuthAccount.builder()
                .username(usernameGenerator.generateUniqueUsername(googleUser.email()))
                .email(googleUser.email())
                .provider(AuthProvider.GOOGLE)
                .providerId(googleUser.providerId())
                .emailVerified(googleUser.emailVerified())
                .build();
        AuthAccount savedAccount = authAccountRepository.save(account);
//        eventPublisher.publish(new AccountCreatedEvent(
//                savedAccount.getEmail(),
//                savedAccount.getUsername()
//        ));
        return savedAccount;
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        AuthAccount account = authAccountRepository.findById(
                SecurityUtils.getAccountId()
        ).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        if (!passwordEncoder.matches(request.currentPassword(), account.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }
        if (passwordEncoder.matches(request.newPassword(), account.getPassword())) {
            throw new BaseException(ErrorCode.SAME_PASSWORD);
        }
        account.setPassword(passwordEncoder.encode(request.newPassword()));
        authAccountRepository.save(account);
        return "Password changed successfully";
    }
}
