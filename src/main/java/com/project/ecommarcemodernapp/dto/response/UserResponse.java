package com.project.ecommarcemodernapp.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User response DTO without sensitive data (no password).
 * Used for API responses to expose user information safely.
 */
public record UserResponse(
        Long id,
        String name,
        String phone,
        String email,
        String username,
        boolean isActive,
        List<OrderResponse> orders,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

