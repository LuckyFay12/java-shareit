package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(UserCreateRequest userCreateRequest);

    @Mapping(target = "id", ignore = true)
    User toUser(UserUpdateRequest userUpdateRequest);

}
