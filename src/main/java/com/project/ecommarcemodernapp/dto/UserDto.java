package com.project.ecommarcemodernapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(Long id,
                      @NotBlank(message = "{user.name.notblank}")
                      String name,
                      @NotBlank(message = "{user.phone.notblank}")
                      String phone,
                      @NotBlank(message = "{user.email.notblank}")
                      @Email(message = "{user.email.invalid}")
                      String email,
                      @NotBlank(message = "{user.username.notblank}")
                      String username,
                      @NotBlank(message = "{user.password.notblank}")
                      @Size(min = 6, message = "{user.password.size}")
                      String password) {
}
