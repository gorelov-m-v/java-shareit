package ru.practicum.shareit.item.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validator.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotBlank(groups = Create.class, message = "Имя должно быть заполнено.")
    String name;
    @NotBlank(groups = Create.class, message = "Описание должно быть заполнено.")
    String description;
    @NotNull(groups = Create.class)
    Boolean available;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(new ItemRequestDto(id, name, description, available));
    }
}