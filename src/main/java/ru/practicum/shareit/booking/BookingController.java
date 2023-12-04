package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Validated BookingRequestDto bookingRequestDto,
                                     HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
        log.debug("Request - {} {} Body: {}", request.getMethod(), request.getRequestURI(), bookingRequestDto);
        BookingResponseDto bookingDtoResponse = bookingService.create(bookingRequestDto, userId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), bookingDtoResponse);
        return bookingDtoResponse;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam("approved") Boolean approved,
                                     HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        BookingResponseDto bookingDtoResponse = bookingService.update(bookingId, userId, approved);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), bookingDtoResponse);
        return bookingDtoResponse;
    }


    @GetMapping("/{bookingId}")
    public BookingResponseDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId,
                                  HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        BookingResponseDto bookingDtoResponse = bookingService.get(bookingId, userId);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), bookingDtoResponse);
        return bookingDtoResponse;
    }

    @GetMapping
    public List<BookingResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                           HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<BookingResponseDto> bookingDtoResponse = bookingService.getAll(userId, state);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), bookingDtoResponse);
        return bookingDtoResponse;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerItemsAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                     HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
        log.debug("Request - {} {}", request.getMethod(), request.getRequestURI());
        List<BookingResponseDto> bookingDtoResponse = bookingService.getOwnerItemsAll(userId, state);
        log.debug("Response - StatusCode: {} Body: {},", response.getStatus(), bookingDtoResponse);
        return bookingDtoResponse;
    }
}
