package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@RequestBody @Validated({Create.class})
                                  UserRequestDto userRequestDto) throws InvalidArgumentException {
        return userService.create(userRequestDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(@RequestBody @Validated({Update.class}) UserRequestDto userRequestDto,
                                  @PathVariable Long userId) throws InvalidArgumentException {
        return userService.update(userRequestDto, userId);
    }

    @GetMapping("/{userId}")
    public UserResponseDto get(@PathVariable Long userId) throws NotFoundException {
        return userService.get(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAll(HttpServletRequest request, HttpServletResponse response) {
        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
