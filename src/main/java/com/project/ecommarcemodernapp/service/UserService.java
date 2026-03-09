package com.project.ecommarcemodernapp.service;

import com.project.ecommarcemodernapp.dto.UserDto;
import com.project.ecommarcemodernapp.dto.request.UserRequest;

public interface UserService {
    UserDto createUser(UserRequest userRequest);
    UserDto updateUser(UserRequest userRequest);
    void deleteUser(Long userId);
    UserDto getUserById(Long userId);

}
