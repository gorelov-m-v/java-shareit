package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        return bookingService.create(bookingRequestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam("approved") Boolean approved) throws NotFoundException {
        return bookingService.update(bookingId, userId, approved);
    }


    @GetMapping("/{bookingId}")
    public BookingResponseDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId) throws NotFoundException {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                           @RequestParam(defaultValue = "0") @Min(0) int from,
                                           @RequestParam(defaultValue = "10") @Min(1) int size) {
        PageRequest page = PageRequest.of(from / size, size).withSort(Sort.Direction.DESC, "start");
        return bookingService.getAll(userId, state, page);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerItemsAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") @Min(0) int from,
                                                     @RequestParam(defaultValue = "10") @Min(1) int size) throws NotFoundException {
        PageRequest page = PageRequest.of(from / size, size).withSort(Sort.Direction.DESC, "start");
        return bookingService.getOwnerItemsAll(userId, state, page);
    }
}
