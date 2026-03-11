package com.project.ecommarcemodernapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "{auth.username.notblank}")
        String username,
        @NotBlank(message = "{auth.password.notblank}")
        String password
) {
}

