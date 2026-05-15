package com.project.ecommarcemodernapp.dto.response;

import java.time.LocalDateTime;

/**
 * Product response DTO for API responses.
 */
public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

