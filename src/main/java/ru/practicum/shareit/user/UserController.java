package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto crete(@RequestBody @Validated({Create.class}) UserRequestDto userRequestDto,
                                 HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {
        log.info("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), userRequestDto);
        User user = userService.creatUser(userRequestDto);
        log.info("Response - StatusCode: {} Body: {},", response.getStatus(), user);
        return UserMapper.userToUserResponseDto(user);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(@RequestBody @Validated({Update.class}) UserRequestDto userRequestDto,
                                  @PathVariable Long userId,
                                  HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {
        log.info("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), userRequestDto);
        User user = userService.update(userRequestDto, userId);
        UserResponseDto userResponseDto = UserMapper.userToUserResponseDto(user);
        log.info("Response - StatusCode: {} Body: {},", response.getStatus(), userResponseDto);
        return userResponseDto;
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId,
                                   HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
        log.info("Request - {} {}", request.getMethod(), request.getRequestURI());
        User user = userService.getUser(userId);
        UserResponseDto userResponseDto = UserMapper.userToUserResponseDto(user);
        log.info("Response - StatusCode: {} Body: {},", response.getStatus(), userResponseDto);
        return userResponseDto;
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<UserResponseDto> users = userService.getAllUsers().stream()
                .map(UserMapper::userToUserResponseDto)
                .collect(Collectors.toList());
        log.info("Response - StatusCode: {} Body: {},", response.getStatus(), users);
        return users;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId,
                           HttpServletRequest request, HttpServletResponse response) {
        log.info("Request - {} {}", request.getMethod(), request.getRequestURI());
        userService.deleteUser(userId);
        log.info("Response - StatusCode: {}", response.getStatus());
    }
}
