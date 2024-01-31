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
}
