package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@RequestBody @Validated({Create.class}) UserRequestDto userRequestDto,
                                  HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {
        log.debug("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), userRequestDto);
        UserResponseDto userResponseDto = userService.create(userRequestDto);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), userResponseDto);
        return userResponseDto;
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(@RequestBody @Validated({Update.class}) UserRequestDto userRequestDto,
                                  @PathVariable Long userId,
                                  HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {
        log.debug("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), userRequestDto);
        UserResponseDto userResponseDto = userService.update(userRequestDto, userId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), userResponseDto);
        return userResponseDto;
    }

    @GetMapping("/{userId}")
    public UserResponseDto get(@PathVariable Long userId,
                               HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        UserResponseDto userResponseDto = userService.getUser(userId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), userResponseDto);
        return userResponseDto;
    }

    @GetMapping
    public List<UserResponseDto> getAll(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<UserResponseDto> users = userService.getAllUsers();
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), users);
        return users;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId,
                       HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        userService.deleteUser(userId);
        log.debug("Response - StatusCode: {}", response.getStatus());
    }
}
