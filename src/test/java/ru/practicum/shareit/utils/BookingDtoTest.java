package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingDtoTest {
    BookingMapper bookingMapper = new BookingMapper();

    private BookingRequestDto bookingRequestDto = new BookingRequestDto(
            LocalDateTime.now(), LocalDateTime.now().plusDays(2), 1L);

    @Test
    void bookingRequestDtoToBookingTest() {
        Booking booking = bookingMapper.bookingRequestDtoToBooking(bookingRequestDto);

        assertEquals(booking.getStart(), bookingRequestDto.getStart());
        assertEquals(booking.getEnd(), bookingRequestDto.getEnd());
    }
}
