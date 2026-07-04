package com.example.backend.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ApiError error;
    private Metadata meta;
    private Pagination pagination;

    public record Metadata(String path, LocalDateTime timestamp) {}

    public static <T> ApiResponse<T> success(T data, String message, String path) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        response.meta = new Metadata(path, LocalDateTime.now());
        return response;
    }

    // paginated success factory method
    public static <T> ApiResponse<T> success(T data, String message, String path, Pagination pagination) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        response.meta = new Metadata(path, LocalDateTime.now());
        response.pagination = pagination;
        return response;
    }

    public static <T> ApiResponse<T> error(ApiError error, String path) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = error;
        response.meta = new Metadata(path, LocalDateTime.now());
        return response;
    }
}
