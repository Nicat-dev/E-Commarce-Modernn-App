package com.project.ecommarcemodernapp.controller;

import com.project.ecommarcemodernapp.dto.UserDto;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.dto.response.UserResponse;
import com.project.ecommarcemodernapp.mapper.UserMapper;
import com.project.ecommarcemodernapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * User management REST controller.
 * Handles user creation, retrieval, update, and deletion operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Create a new user (Admin only).
     *
     * @param userRequest User registration data
     * @return Created user response
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Creating user with username: {}", userRequest.username());
        UserDto userDto = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userRequest));
    }

    /**
     * Get user by ID.
     *
     * @param id User ID
     * @return User response
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.debug("Fetching user with id: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Update user information.
     *
     * @param id User ID
     * @param userRequest Updated user data
     * @return Updated user response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        log.info("Updating user with id: {}", id);
        return ResponseEntity.ok(userService.updateUser(userRequest, id));
    }

    /**
     * Delete a user (Admin only).
     *
     * @param id User ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

