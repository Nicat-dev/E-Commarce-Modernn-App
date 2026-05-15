package com.project.ecommarcemodernapp.controller;

import com.project.ecommarcemodernapp.dto.request.LoginRequest;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.dto.response.LoginResponse;
import com.project.ecommarcemodernapp.dto.response.MessageResponse;
import com.project.ecommarcemodernapp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication REST controller.
 * Handles user login and registration endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Authenticate user and return JWT token.
     * Public endpoint - no authentication required.
     *
     * @param loginRequest User credentials (username and password)
     * @return JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for username: {}", loginRequest.username());
        LoginResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Register a new user.
     * Public endpoint - no authentication required.
     *
     * @param userRequest User registration details
     * @return Success message with user creation confirmation
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody UserRequest userRequest) {
        log.info("Registration attempt for username: {}", userRequest.username());
        MessageResponse response = authenticationService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

