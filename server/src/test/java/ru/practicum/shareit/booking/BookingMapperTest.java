package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    @Test
    void convertToDto_ShouldMapBookingToDto() {
        User booker = new User();
        booker.setUserId(1L);
        Item item = new Item();
        item.setItemId(1L);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);

        BookingDto result = BookingMapper.convertToDto(booking);

        assertEquals(1L, result.getId());
        assertEquals(booking.getStartTime(), result.getStartTime());
        assertEquals(booking.getEndTime(), result.getEndTime());
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        assertEquals(1L, result.getBooker().getId());
        assertEquals(1L, result.getItem().getId());
    }

    @Test
    void convertToEntity_ShouldMapDtoToBooking() {
        User booker = new User();
        booker.setUserId(1L);
        Item item = new Item();
        item.setItemId(1L);
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setStatus(BookingStatus.APPROVED);

        Booking result = BookingMapper.convertToEntity(dto, item, booker);

        assertEquals(1L, result.getId());
        assertEquals(dto.getStartTime(), result.getStartTime());
        assertEquals(dto.getEndTime(), result.getEndTime());
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        assertEquals(booker, result.getBooker());
        assertEquals(item, result.getItem());
    }
}