package com.project.ecommarcemodernapp.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Order response DTO with all order details including items.
 */
public record OrderResponse(
        Long id,
        String orderCode,
        Long userId,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

