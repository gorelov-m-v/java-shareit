package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public class UserMapper {

    public static User userFromUserRequestDto(UserRequestDto userRequestDto) {
        return new User(
                UserRepositoryImpl.generateId(),
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

