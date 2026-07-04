package com.example.backend.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
//        @NotBlank(message = "Username is required")
//        @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
        String username,
//        @NotBlank(message = "Email is required")
//        @Email(message = "Invalid email format")
        String email,
//        @NotBlank(message = "Password is required")
//        @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
//        @Pattern(
//                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
//                message = "Password must contain uppercase, lowercase, number and special character"
//        )
        String password
) {
}
