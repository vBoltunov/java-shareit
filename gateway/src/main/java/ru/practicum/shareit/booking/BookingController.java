package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.util.HeaderConstants;
import ru.practicum.shareit.util.PathConstants;

@Controller
@RequestMapping(path = PathConstants.BOOKINGS_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
											  @RequestParam(name = "state", defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Sending GET request for bookings with state {}, userId={}, from={}, size={}",
				stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping(PathConstants.BOOKING_ID_PATH)
	public ResponseEntity<Object> getBooking(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
											 @PathVariable("booking-id") @Positive Long bookingId) {
		log.info("Sending GET request for booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Sending POST request for booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@PatchMapping(PathConstants.BOOKING_ID_PATH)
	public ResponseEntity<Object> approveBooking(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long ownerId,
									 @PathVariable("booking-id") @Positive Long bookingId,
									 @RequestParam @NotNull boolean approved) {
		log.info("Sending PATCH request for booking with id: {} by user with id: {}", bookingId, ownerId);
		return bookingClient.approveBooking(bookingId, ownerId, approved);
	}
}