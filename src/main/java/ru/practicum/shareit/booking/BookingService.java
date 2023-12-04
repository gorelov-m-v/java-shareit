package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(BookingRequestDto bookingDto, Long userId) throws NotFoundException;

    BookingResponseDto update(Long bookingId, Long userId, Boolean approved) throws NotFoundException;

    BookingResponseDto get(Long bookingId, Long userId) throws NotFoundException;

    List<BookingResponseDto> getAll(Long userId, String state);

    List<BookingResponseDto> getOwnerItemsAll(Long userId, String state) throws NotFoundException;
}
