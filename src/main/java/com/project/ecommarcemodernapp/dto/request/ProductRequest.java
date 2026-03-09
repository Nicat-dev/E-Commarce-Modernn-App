package com.project.ecommarcemodernapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "{product.name.notblank}")
        String name,
        @NotBlank(message = "{product.description.notblank}")
        String description,
        @NotNull(message = "{product.price.notnull}")
        @Positive(message = "{product.price.positive}")
        Double price) {
}
