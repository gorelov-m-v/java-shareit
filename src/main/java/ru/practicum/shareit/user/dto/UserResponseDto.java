package ru.practicum.shareit.user.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    Long id;
    String name;
    String email;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(new UserResponseDto(id ,name, email));
    }
}
