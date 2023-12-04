package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static User userFromUserRequestDto(UserRequestDto userRequestDto) {
        return new User(
                userRequestDto.getId(),
                userRequestDto.getName(),
                userRequestDto.getEmail());
    }

    public static UserResponseDto userToUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}

