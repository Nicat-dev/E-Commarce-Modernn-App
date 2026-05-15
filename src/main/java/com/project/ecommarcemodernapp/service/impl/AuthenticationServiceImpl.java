package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.request.LoginRequest;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.dto.response.LoginResponse;
import com.project.ecommarcemodernapp.dto.response.MessageResponse;
import com.project.ecommarcemodernapp.exception.ApplicationException;
import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;
import com.project.ecommarcemodernapp.model.Users;
import com.project.ecommarcemodernapp.repository.UserRepository;
import com.project.ecommarcemodernapp.util.JwtProvider;
import com.project.ecommarcemodernapp.service.AuthenticationService;
import com.project.ecommarcemodernapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Authentication service implementation handling user login and registration.
 * Manages JWT token generation and user registration with proper error handling.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String USER_REGISTERED_MESSAGE = "User registered successfully";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid username or password";

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Authenticates user with username and password, returns JWT token.
     *
     * @param loginRequest Login credentials
     * @return LoginResponse with JWT token
     * @throws ApplicationException if credentials are invalid
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            return authenticateUser(loginRequest)
                    .map(token -> buildLoginResponse(token, loginRequest.username()))
                    .orElseThrow(() -> {
                        log.warn("Failed login attempt for username: {}", loginRequest.username());
                        return new ApplicationException(ExceptionStatus.INVALID_CREDENTIALS);
                    });
        } catch (BadCredentialsException e) {
            log.warn("Bad credentials for username: {}", loginRequest.username());
            throw new ApplicationException(ExceptionStatus.INVALID_CREDENTIALS);
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Registers a new user with provided credentials.
     *
     * @param userRequest User registration details
     * @return MessageResponse indicating successful registration
     * @throws ApplicationException if registration fails
     */
    @Override
    public MessageResponse register(UserRequest userRequest) {
        return Optional.of(userRequest)
                .map(request -> {
                    userService.createUser(request);
                    log.info("User {} registered successfully", request.username());
                    return new MessageResponse(USER_REGISTERED_MESSAGE, true);
                })
                .orElseThrow(() -> {
                    log.error("Registration failed");
                    return new RuntimeException("Registration failed");
                });
    }

    /**
     * Authenticates user credentials using AuthenticationManager.
     *
     * @param loginRequest Login credentials
     * @return Optional containing JWT token if authentication succeeds
     */
    private Optional<String> authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );
            return Optional.of(jwtProvider.generateToken(authentication));
        } catch (BadCredentialsException e) {
            return Optional.empty();
        }
    }

    /**
     * Builds login response with user details and JWT token.
     *
     * @param token JWT token
     * @param username Username of authenticated user
     * @return LoginResponse with user details
     */
    private LoginResponse buildLoginResponse(String token, String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    log.info("User {} logged in successfully", username);
                    return new LoginResponse(
                            token,
                            "Bearer",
                            user.getId(),
                            user.getUsername(),
                            user.getEmail()
                    );
                })
                .orElseThrow(() -> new ApplicationException(ExceptionStatus.USER_NOT_FOUND));
    }
}
