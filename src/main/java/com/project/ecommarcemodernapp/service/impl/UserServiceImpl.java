package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.UserDto;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.exception.ApplicationException;
import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;
import com.project.ecommarcemodernapp.mapper.UserMapper;
import com.project.ecommarcemodernapp.model.Users;
import com.project.ecommarcemodernapp.repository.UserRepository;
import com.project.ecommarcemodernapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

import static com.project.ecommarcemodernapp.exception.ApplicationException.throwIf;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Validation predicates
    private final Predicate<String> isEmailUnique = email -> !userRepository.existsByEmail(email);
    private final Predicate<String> isUsernameUnique = username -> !userRepository.existsByUsername(username);

    @Override
    public UserDto createUser(UserRequest userRequest) {
        validateUserRequest(userRequest);
        Users user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserRequest userRequest, Long id) {
        return userRepository.findById(id).map((user) -> {
            validateUserRequest(userRequest);
            user.setId(id);
            user = userMapper.toEntity(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.password()));
            return userMapper.toDto(userRepository.save(user));
        }).orElseThrow(() -> new ApplicationException(ExceptionStatus.USER_NOT_FOUND));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(user(userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.toDto(user(userId));
    }

    @Override
    public Users user(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionStatus.USER_NOT_FOUND));
    }


    private void validateUserRequest(UserRequest userRequest) {

        throwIf(!isEmailUnique.test(userRequest.email()), ExceptionStatus.EMAIL_ALREADY_EXISTS);
        throwIf(!isUsernameUnique.test(userRequest.username()), ExceptionStatus.USERNAME_ALREADY_EXISTS);

    }
}
