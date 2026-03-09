package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.UserDto;
import com.project.ecommarcemodernapp.dto.request.UserRequest;
import com.project.ecommarcemodernapp.model.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface UserMapper {
    UserDto toDto(Users user);

    Users toEntity(UserRequest request);

    List<UserDto> toDtoList(List<Users> users);
}
