package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface  UserService {

    UserResponseDto create(UserRequestDto userDtoRequest) throws InvalidArgumentException;

    UserResponseDto update(UserRequestDto userDtoRequest, Long userId) throws InvalidArgumentException;

    UserResponseDto get(Long userId) throws NotFoundException;

    void delete(Long userId);

    List<UserResponseDto> getAll();
}
