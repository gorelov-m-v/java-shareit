package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Validated BookingRequestDto bookingRequestDto) throws NotFoundException {
        BookingResponseDto bookingDtoResponse = bookingService.create(bookingRequestDto, userId);
        return bookingDtoResponse;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam("approved") Boolean approved) throws NotFoundException {
        BookingResponseDto bookingDtoResponse = bookingService.update(bookingId, userId, approved);
        return bookingDtoResponse;
    }


    @GetMapping("/{bookingId}")
    public BookingResponseDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId) throws NotFoundException {
        BookingResponseDto bookingDtoResponse = bookingService.get(bookingId, userId);
        return bookingDtoResponse;
    }

    @GetMapping
    public List<BookingResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                           @RequestParam(defaultValue = "0") @Min(0) int from,
                                           @RequestParam(defaultValue = "10") @Min(1) int size) {
        PageRequest page = PageRequest.of(from / size, size).withSort(Sort.Direction.DESC, "start");
        List<BookingResponseDto> bookingDtoResponse = bookingService.getAll(userId, state, page);
        return bookingDtoResponse;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerItemsAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "10") @Min(1) int size) throws NotFoundException {
        PageRequest page = PageRequest.of(from / size, size).withSort(Sort.Direction.DESC, "start");
        List<BookingResponseDto> bookingDtoResponse = bookingService.getOwnerItemsAll(userId, state, page);
        return bookingDtoResponse;
    }
}
