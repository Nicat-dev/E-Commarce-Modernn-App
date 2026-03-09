package com.project.ecommarcemodernapp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
        @NotNull(message = "{orderitem.productid.notnull}")
        Long productId,
        @NotNull(message = "{orderitem.quantity.notnull}")
        @Positive(message = "{orderitem.quantity.positive}")
        Integer quantity) {
}
