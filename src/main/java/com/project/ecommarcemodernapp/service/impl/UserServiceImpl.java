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

import static com.project.ecommarcemodernapp.exception.ApplicationException.throwIf;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserRequest userRequest) {
        validateUserRequestForCreate(userRequest);
        Users user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setActive(userRequest.isActive());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserRequest userRequest, Long id) {
        Users existingUser = getUser(id);

        validateUserRequestForUpdate(userRequest, id);

        existingUser.setName(userRequest.name());
        existingUser.setPhone(userRequest.phone());
        existingUser.setEmail(userRequest.email());
        existingUser.setUsername(userRequest.username());
        existingUser.setPassword(passwordEncoder.encode(userRequest.password()));
        existingUser.setActive(userRequest.isActive());

        return userMapper.toDto(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(getUser(userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.toDto(getUser(userId));
    }

    @Override
    public Users user(Long id) {
        return getUser(id);
    }

    private Users getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionStatus.USER_NOT_FOUND));
    }

    private void validateUserRequestForCreate(UserRequest userRequest) {
        throwIf(userRepository.existsByEmail(userRequest.email()), ExceptionStatus.EMAIL_ALREADY_EXISTS);
        throwIf(userRepository.existsByUsername(userRequest.username()), ExceptionStatus.USERNAME_ALREADY_EXISTS);
    }

    private void validateUserRequestForUpdate(UserRequest userRequest, Long userId) {
        userRepository.findByEmailAndIdNot(userRequest.email(), userId).ifPresent(
                _ -> {
                throw new ApplicationException(ExceptionStatus.EMAIL_ALREADY_EXISTS);
            }
        );
        userRepository.findByUsernameAndIdNot(userRequest.username(), userId).ifPresent(
                _ -> {
                throw new ApplicationException(ExceptionStatus.USERNAME_ALREADY_EXISTS);
            }
        );
    }
}
