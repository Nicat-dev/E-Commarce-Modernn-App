package com.project.ecommarcemodernapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest(
        @NotNull(message = "{order.userid.notnull}")
        Long userId,
        @NotEmpty(message = "{order.items.notempty}")
        @Valid
        List<OrderItemRequest> items) {
}
