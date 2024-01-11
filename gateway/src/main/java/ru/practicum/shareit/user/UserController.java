package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity create(@RequestBody @Validated({Create.class})
                                 UserRequestDto userRequestDto) throws InvalidArgumentException {
        return userClient.create(userRequestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity update(@RequestBody @Validated({Update.class}) UserRequestDto userRequestDto,
                                 @PathVariable Long userId) throws InvalidArgumentException {
        return userClient.update(userId, userRequestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity get(@PathVariable Long userId) throws NotFoundException {
        return userClient.get(userId);
    }

    @GetMapping
    public ResponseEntity getAll(HttpServletRequest request, HttpServletResponse response) {
        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userClient.delete(userId);
    }
}
