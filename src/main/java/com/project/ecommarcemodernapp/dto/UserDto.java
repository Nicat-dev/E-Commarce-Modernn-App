package com.project.ecommarcemodernapp.dto;

import java.time.LocalDateTime;

public record UserDto(Long id,
                      String name,
                      String phone,
                      String email,
                      String username,
                      boolean isActive,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
}
