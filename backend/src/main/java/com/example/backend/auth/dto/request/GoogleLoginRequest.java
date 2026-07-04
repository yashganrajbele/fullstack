package com.example.backend.auth.dto.request;

public record GoogleLoginRequest(
        String idToken
) {
}

/*
Request body:
{
  "idToken": "eyJhbGciOiJSUzI1NiIs..."
}
*/