package com.project.ecommarcemodernapp.service;

import com.project.ecommarcemodernapp.dto.request.LoginRequest;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.dto.response.LoginResponse;
import com.project.ecommarcemodernapp.dto.response.MessageResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
    MessageResponse register(UserRequest userRequest);
}

