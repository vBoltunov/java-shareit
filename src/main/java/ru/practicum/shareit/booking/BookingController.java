package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import static ru.practicum.shareit.util.HeaderConstants.USER_ID_HEADER;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;
    public static final String BOOKING_ID_PATH = "/{booking-id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Creating booking for user with id: {}", bookerId);
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping(BOOKING_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                     @PathVariable("booking-id") Long bookingId,
                                     @RequestParam boolean approved) {
        log.info("Approving booking with id: {} by user with id: {}", bookingId, ownerId);
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }

    @GetMapping(BOOKING_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@RequestHeader(USER_ID_HEADER) Long userId,
                                     @PathVariable("booking-id") Long bookingId) {
        log.info("Fetching booking with id: {} for user with id: {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsByBookerId(
            @RequestHeader(USER_ID_HEADER) Long bookerId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Fetching bookings for booker with id: {} and state: {}", bookerId, state);
        BookingState bookingState = BookingState.valueOf(state.toUpperCase());
        return bookingService.getBookingsByBookerIdAndState(bookerId, bookingState);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsByOwnerId(@RequestHeader(USER_ID_HEADER) Long ownerId) {
        log.info("Fetching bookings for owner with id: {}", ownerId);
        return bookingService.getBookingsByOwnerId(ownerId);
    }
}