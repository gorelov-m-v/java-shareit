package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemWithCommentResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentResponseDto> comments;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;

    public ItemWithCommentResponseDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
