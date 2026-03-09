package com.project.ecommarcemodernapp.dto;

import java.time.LocalDateTime;

public record ProductDto(Long id,
                         String name,
                         String description,
                         Double price,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt) {
}
