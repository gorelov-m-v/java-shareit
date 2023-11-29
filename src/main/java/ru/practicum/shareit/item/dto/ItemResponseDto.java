package ru.practicum.shareit.item.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(new ItemResponseDto(id, name, description, available));
    }
}
