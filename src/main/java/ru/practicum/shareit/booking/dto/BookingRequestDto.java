package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Builder
public class BookingRequestDto {

    @Future
    @NotNull
    private LocalDateTime start;

    @Future
    @NotNull
    private LocalDateTime end;

    private Long itemId;
}
