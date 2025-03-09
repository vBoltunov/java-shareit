package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long bookerId);

    BookingDto approveBooking(Long bookingId, Long ownerId, boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> getBookingsByOwnerId(Long ownerId);

    Collection<BookingDto> getBookingsByBookerIdAndState(Long bookerId, BookingState state);
}
