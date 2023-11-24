package ru.practicum.shareit.user.dto;

import com.google.gson.Gson;
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

    @NotBlank(groups = Create.class, message = "name - не может быть пустым")
    @NotNull(groups = Create.class, message = "name - обязательный")
    String name;

    @NotNull(groups = Create.class, message = "email - обязательный параметр")
    @Email(groups = {Create.class, Update.class}, message = "email - невалидный")
    String email;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(new UserRequestDto(name, email));
    }
}
