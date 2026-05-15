package com.project.ecommarcemodernapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * User update request DTO (without password).
 * This is used for updating user profile information.
 * Password changes should use a dedicated endpoint.
 */
public record UserUpdateRequest(
        @NotBlank(message = "{user.name.notblank}")
        String name,

        @NotBlank(message = "{user.phone.notblank}")
        String phone,

        @NotBlank(message = "{user.email.notblank}")
        @Email(message = "{user.email.invalid}")
        String email,

        boolean isActive) {
}

