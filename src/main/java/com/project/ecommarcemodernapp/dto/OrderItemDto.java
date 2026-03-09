package com.project.ecommarcemodernapp.dto;

import java.time.LocalDateTime;

public record OrderItemDto(Long id,
                           Long productId,
                           Integer quantity,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
}
