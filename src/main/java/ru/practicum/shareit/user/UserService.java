package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface  UserService {

    User creatUser(UserRequestDto userDtoRequest) throws InvalidArgumentException;

    User update(UserRequestDto userDtoRequest, Long userId) throws InvalidArgumentException;

    User getUser(Long userId) throws NotFoundException;

    void deleteUser(Long userId);

    List<User> getAllUsers();
}
