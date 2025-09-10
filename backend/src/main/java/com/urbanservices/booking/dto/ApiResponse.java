package com.urbanservices.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String error;
    private String path;

    // Success response with data
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data, LocalDateTime.now(), null, null);
    }

    // Success response with message and data
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), null, null);
    }

    // Success response with message only
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null, LocalDateTime.now(), null, null);
    }

    // Error response with message
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, null, LocalDateTime.now(), message, null);
    }

    // Error response with message and path
    public static <T> ApiResponse<T> error(String message, String path) {
        return new ApiResponse<>(false, null, null, LocalDateTime.now(), message, path);
    }

    // Error response with message, error, and path
    public static <T> ApiResponse<T> error(String message, String error, String path) {
        return new ApiResponse<>(false, null, null, LocalDateTime.now(), message + ": " + error, path);
    }
}
