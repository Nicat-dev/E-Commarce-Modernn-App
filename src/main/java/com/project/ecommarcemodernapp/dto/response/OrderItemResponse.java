package com.project.ecommarcemodernapp.dto.response;

import java.time.LocalDateTime;

/**
 * OrderItem response DTO with product and order item details.
 */
public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        Double productPrice,
        Integer quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

