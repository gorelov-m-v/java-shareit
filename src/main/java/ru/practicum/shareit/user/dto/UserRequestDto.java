package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validator.UserCreate;
import ru.practicum.shareit.validator.UserUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(groups = UserCreate.class, message = "name - не может быть пустым")
    @NotNull(groups = UserCreate.class, message = "name - обязательный")
    String name;

    @NotNull(groups = UserCreate.class, message = "email - обязательный параметр")
    @Email(groups = {UserCreate.class, UserUpdate.class}, message = "email - невалидный")
    String email;
}
