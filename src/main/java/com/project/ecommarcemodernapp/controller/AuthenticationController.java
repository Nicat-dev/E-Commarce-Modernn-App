package com.project.ecommarcemodernapp.controller;

import com.project.ecommarcemodernapp.dto.request.LoginRequest;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.dto.response.LoginResponse;
import com.project.ecommarcemodernapp.dto.response.MessageResponse;
import com.project.ecommarcemodernapp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody UserRequest userRequest) {
        MessageResponse response = authenticationService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

