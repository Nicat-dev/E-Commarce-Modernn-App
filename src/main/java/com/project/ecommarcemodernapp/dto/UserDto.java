package com.project.ecommarcemodernapp.dto;

import java.time.LocalDateTime;

public record UserDto(Long id,
                      String name,
                      String phone,
                      String email,
                      String username,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
}
