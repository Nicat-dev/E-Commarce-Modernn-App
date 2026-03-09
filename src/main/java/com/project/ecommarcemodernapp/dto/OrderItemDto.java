package com.project.ecommarcemodernapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemDto(Long id,
                           @NotNull(message = "{orderitem.orderid.notnull}")
                           Long orderId,
                           @NotNull(message = "{orderitem.productid.notnull}")
                           Long productId,
                           @NotNull(message = "{orderitem.quantity.notnull}")
                           @Positive(message = "{orderitem.quantity.positive}")
                           Integer quantity) {
}
