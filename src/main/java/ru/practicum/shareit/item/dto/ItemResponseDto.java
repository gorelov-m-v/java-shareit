package ru.practicum.shareit.item.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentResponseDto> comments;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;

    public ItemResponseDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(new ItemResponseDto(id, name, description, available, comments, lastBooking, nextBooking));
    }
}
