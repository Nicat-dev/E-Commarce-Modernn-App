package com.project.ecommarcemodernapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(Long id,
                      String name,
                      String phone,
                      String email,
                      String username,
                      boolean isActive,
                      List<OrderDto> orders,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
}
