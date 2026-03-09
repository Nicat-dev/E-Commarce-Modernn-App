package com.project.ecommarcemodernapp.service;

import com.project.ecommarcemodernapp.dto.UserDto;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.model.Users;

public interface UserService {
    UserDto createUser(UserRequest userRequest);
    UserDto updateUser(UserRequest userRequest,Long id);
    void deleteUser(Long userId);
    UserDto getUserById(Long userId);
    Users user(Long id);
}
