package com.project.ecommarcemodernapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderDto(Long id,
                       @NotNull(message = "{order.userid.notnull}")
                       Long userId,
                       @NotEmpty(message = "{order.items.notempty}")
                       List<OrderItemDto> items) {
}
