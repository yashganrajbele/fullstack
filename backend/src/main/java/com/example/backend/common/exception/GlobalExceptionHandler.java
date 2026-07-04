package com.example.backend.common.exception;

import com.example.backend.common.dto.ApiError;
import com.example.backend.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(BaseException ex, HttpServletRequest request) {
        return buildResponse(ex.getErrorCode(), null, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> details.put(e.getField(), e.getDefaultMessage()));
        return buildResponse(ErrorCode.BAD_REQUEST, details, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildResponse(ErrorCode.CONFLICT, null, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        return buildResponse(ErrorCode.UNAUTHORIZED, null, request);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
        return buildResponse(ErrorCode.FORBIDDEN, null, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildResponse(ErrorCode.INTERNAL_SERVER_ERROR, null, request);
    }

    private ResponseEntity<ApiResponse<?>> buildResponse(ErrorCode ec, Object details, HttpServletRequest request) {
        ApiError error = new ApiError(ec.getCode(), ec.getMessage(), ec.getStatus(), details);
        return ResponseEntity.status(ec.getStatus()).body(ApiResponse.error(error, request.getRequestURI()));
    }
}
