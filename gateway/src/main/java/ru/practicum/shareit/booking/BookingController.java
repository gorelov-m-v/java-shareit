package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity create(@RequestHeader("X-Sharer-User-Id") Long userId,
								 @RequestBody @Validated BookingRequestDto bookingRequestDto) {
		return bookingClient.create(userId, bookingRequestDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity update(@RequestHeader("X-Sharer-User-Id") Long userId,
								 @PathVariable Long bookingId,
								 @RequestParam("approved") Boolean approved) {
		return bookingClient.update(userId, bookingId, approved);
	}


	@GetMapping("/{bookingId}")
	public ResponseEntity get(@RequestHeader("X-Sharer-User-Id") Long userId,
							  @PathVariable Long bookingId) {
		return bookingClient.get(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
								 @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
								 @RequestParam(defaultValue = "0") @Min(0) int from,
								 @RequestParam(defaultValue = "10") @Min(1) int size) {
		return bookingClient.getBookings("", userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity getOwnerItemsAll(@RequestHeader("X-Sharer-User-Id") Long userId,
										   @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
										   @RequestParam(defaultValue = "0") @Min(0) int from,
										   @RequestParam(defaultValue = "10") @Min(1) int size) {
		return bookingClient.getBookings("/owner", userId, state, from, size);
	}
}
