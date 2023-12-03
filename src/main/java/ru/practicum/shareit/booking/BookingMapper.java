package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {
    public static Booking bookingRequestDtoToBooking(BookingRequestDto bookingRequestDto) {
        return new Booking(bookingRequestDto.getStart(), bookingRequestDto.getEnd());
    }

    public static BookingResponseDto bookingToBookingResponseDto(Booking booking) {
        return new BookingResponseDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                UserMapper.userToUserResponseDto(booking.getBooker()),
                ItemMapper.itemToItemResponseDto(booking.getItem()));
    }

    public static BookingShortDto bookingToBookingShortDto(Booking booking) {
        BookingShortDto bookingShortDto = new BookingShortDto(booking.getId(), booking.getItem().getId(),
                booking.getBooker().getId(), booking.getStart(), booking.getEnd());
        return bookingShortDto;
    }
}
