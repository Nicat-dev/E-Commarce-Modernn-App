package com.project.ecommarcemodernapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest(
        @NotBlank(message = "{order.ordercode.notblank}")
        String orderCode,
        @NotNull(message = "{order.userid.notnull}")
        Long userId,
        @NotEmpty(message = "{order.items.notempty}")
        @Valid
        List<OrderItemRequest> items) {
}
