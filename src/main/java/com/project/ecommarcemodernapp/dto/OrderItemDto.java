package com.project.ecommarcemodernapp.dto;

import java.time.LocalDateTime;

/**
 * OrderItem DTO for internal use and mapping.
 */
public record OrderItemDto(
        Long id,
        Long productId,
        Integer quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
