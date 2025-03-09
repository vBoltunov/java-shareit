package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.util.HeaderConstants;
import ru.practicum.shareit.util.PathConstants;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = PathConstants.BOOKINGS_PATH)
public class BookingController {
    private final BookingServiceImpl bookingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsByBookerId(
            @RequestHeader(HeaderConstants.USER_ID_HEADER) Long bookerId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Received GET request for bookings for booker with id: {} and state: {}", bookerId, state);
        BookingState bookingState = BookingState.valueOf(state.toUpperCase());
        return bookingService.getBookingsByBookerIdAndState(bookerId, bookingState);
    }

    @GetMapping(PathConstants.OWNER_PATH)
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsByOwnerId(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long ownerId) {
        log.info("Received GET request for bookings for owner with id: {}", ownerId);
        return bookingService.getBookingsByOwnerId(ownerId);
    }

    @GetMapping(PathConstants.BOOKING_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long userId,
                                     @PathVariable("booking-id") Long bookingId) {
        log.info("Received GET request for booking with id: {} for user with id: {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long bookerId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Received POST request for booking for user with id: {}", bookerId);
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping(PathConstants.BOOKING_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long ownerId,
                                     @PathVariable("booking-id") Long bookingId,
                                     @RequestParam boolean approved) {
        log.info("Received PATCH request for booking with id: {} by user with id: {}", bookingId, ownerId);
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }
}