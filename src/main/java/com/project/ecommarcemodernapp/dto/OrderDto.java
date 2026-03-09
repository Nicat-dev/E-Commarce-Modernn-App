package com.project.ecommarcemodernapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Long id,
                       Long userId,
                       List<OrderItemDto> items,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
}
