package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.validator.UserCreate;
import ru.practicum.shareit.validator.UserUpdate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto crete(@RequestBody @Validated({UserCreate.class}) UserRequestDto userRequestDto) throws InvalidArgumentException {
        User user = userService.creatUser(userRequestDto);
        log.info("создан пользователь с ID - " + user.getId());
        return UserMapper.userToUserResponseDto(user);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(@RequestBody @Validated({UserUpdate.class}) UserRequestDto userRequestDto,
                                  @PathVariable Long userId) throws InvalidArgumentException {
        User user = userService.update(userRequestDto, userId);
        log.info("обновлен пользователь с ID - " + user.getId());
        return UserMapper.userToUserResponseDto(user);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId) throws NotFoundException {
        User user = userService.getUser(userId);
        log.info("получен пользователь с ID - " + user.getId());
        return UserMapper.userToUserResponseDto(user);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        List<UserResponseDto> usersResp = new ArrayList<>();
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            usersResp.add(UserMapper.userToUserResponseDto(user));
        }
        log.info("получен список из "  + usersResp.size() + " пользователей ");
        return usersResp;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("удален пользователь с ID " + userId);
        userService.deleteUser(userId);
    }

    @PutMapping  //  ("/{userId}")
    public void put() throws NotFoundException {
        throw new NotFoundException("такого эндпоинта не существует");
    }
}
