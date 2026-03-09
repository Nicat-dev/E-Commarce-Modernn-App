package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.UserDto;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.mapper.UserMapper;
import com.project.ecommarcemodernapp.repository.UserRepository;
import com.project.ecommarcemodernapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserDto updateUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public UserDto getUserById(Long userId) {
        return null;
    }
}
