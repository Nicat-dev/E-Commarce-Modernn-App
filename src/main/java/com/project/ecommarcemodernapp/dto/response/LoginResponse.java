package com.project.ecommarcemodernapp.dto.response;

public record LoginResponse(
        String token,
        String type,
        Long id,
        String username,
        String email
) {
}

