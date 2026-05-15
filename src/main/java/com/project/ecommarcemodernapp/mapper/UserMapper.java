package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.UserDto;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.dto.request.UserUpdateRequest;
import com.project.ecommarcemodernapp.dto.response.UserResponse;
import com.project.ecommarcemodernapp.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper for User entity to/from DTOs.
 * Handles mapping between entity and various DTO types (request, response, internal DTO).
 */
@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface UserMapper {

    // Request to Entity
    Users toEntity(UserRequest request);

    // Entity to DTO
    UserDto toDto(Users user);


    // List conversions
    List<UserDto> toDtoList(List<Users> users);

    List<UserResponse> toResponseList(List<Users> users);

    // Update mapping
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget Users entity);
}
