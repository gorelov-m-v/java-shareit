package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validator.Create;
import ru.practicum.shareit.validator.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserRequestDto {

    private Long id;

    @NotBlank(groups = Create.class, message = "name - не может быть пустым")
    @NotNull(groups = Create.class, message = "name - обязательный")
    String name;

    @NotNull(groups = Create.class, message = "email - обязательный параметр")
    @Email(groups = {Create.class, Update.class}, message = "email - невалидный")
    String email;
}
