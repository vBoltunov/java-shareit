package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private User booker;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;
    private LocalDateTime fixedTime;

    @BeforeEach
    void setUp() throws Exception {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        if (autoCloseable != null) {
            autoCloseable.close();
        }

        fixedTime = LocalDateTime.of(2025, 3, 9, 10, 0, 0);

        booker = new User();
        booker.setUserId(1L);
        booker.setName("Booker");
        booker.setEmail("booker@example.com");

        owner = new User();
        owner.setUserId(2L);
        owner.setName("Owner");
        owner.setEmail("owner@example.com");

        item = new Item();
        item.setItemId(1L);
        item.setOwnerId(owner.getUserId());
        item.setAvailable(true);
        item.setName("Item");
        item.setDescription("Description");

        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartTime(fixedTime.plusHours(1));
        booking.setEndTime(fixedTime.plusHours(2));

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(1L);
        bookingDto.setStartTime(fixedTime.plusHours(1));
        bookingDto.setEndTime(fixedTime.plusHours(2));
        bookingDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    void getBookingsByOwnerId_ShouldReturnBookings() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(userRepository.existsById(2L)).thenReturn(true);
            when(bookingRepository.findByItem_OwnerId(2L)).thenReturn(Collections.singletonList(booking));
            mapper.when(() -> BookingMapper.convertToDto(booking)).thenReturn(bookingDto);

            Collection<BookingDto> result = bookingService.getBookingsByOwnerId(2L);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getBookingsByOwnerId_ShouldThrowNotFoundException_WhenOwnerNotFound() {
        when(userRepository.existsById(2L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsByOwnerId(2L));
    }

    // Остальные тесты остаются без изменений
    @Test
    void createBooking_ShouldCreateBooking() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(userRepository.findById(1L)).thenReturn(Optional.of(booker));
            when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            mapper.when(() -> BookingMapper.convertToEntity(bookingDto, item, booker)).thenReturn(booking);
            when(bookingRepository.save(booking)).thenReturn(booking);
            mapper.when(() -> BookingMapper.convertToDto(booking)).thenReturn(bookingDto);

            BookingDto result = bookingService.createBooking(bookingDto, 1L);

            assertEquals(1L, result.getId());
            assertEquals(BookingStatus.WAITING, booking.getStatus());
        }
    }

    @Test
    void createBooking_ShouldThrowValidationException_WhenDatesNull() {
        bookingDto.setStartTime(null);
        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookingDto, 1L));
    }

    @Test
    void createBooking_ShouldThrowNotFoundException_WhenBookerNotFound() {
        bookingDto.setStartTime(fixedTime.plusHours(1));
        bookingDto.setEndTime(fixedTime.plusHours(2));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, 1L));
    }

    @Test
    void createBooking_ShouldThrowValidationException_WhenItemNotAvailable() {
        bookingDto.setStartTime(fixedTime.plusHours(1));
        bookingDto.setEndTime(fixedTime.plusHours(2));
        item.setAvailable(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookingDto, 1L));
    }

    @Test
    void createBooking_ShouldThrowNotFoundException_WhenOwnerBooksOwnItem() {
        bookingDto.setStartTime(fixedTime.plusHours(1));
        bookingDto.setEndTime(fixedTime.plusHours(2));
        when(userRepository.findById(2L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, 2L));
    }

    @Test
    void createBooking_ShouldThrowValidationException_WhenDatesInvalid() {
        bookingDto.setStartTime(fixedTime.plusHours(2));
        bookingDto.setEndTime(fixedTime.plusHours(1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookingDto, 1L));
    }

    @Test
    void approveBooking_ShouldApproveBooking() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            when(bookingRepository.save(booking)).thenReturn(booking);
            mapper.when(() -> BookingMapper.convertToDto(booking)).thenReturn(bookingDto);

            BookingDto result = bookingService.approveBooking(1L, 2L, true);

            assertEquals(BookingStatus.APPROVED, booking.getStatus());
            assertEquals(1L, result.getId());
        }
    }

    @Test
    void approveBooking_ShouldRejectBooking() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            when(bookingRepository.save(booking)).thenReturn(booking);
            mapper.when(() -> BookingMapper.convertToDto(booking)).thenReturn(bookingDto);

            BookingDto result = bookingService.approveBooking(1L, 2L, false);

            assertEquals(BookingStatus.REJECTED, booking.getStatus());
            assertEquals(1L, result.getId());
        }
    }

    @Test
    void approveBooking_ShouldThrowForbiddenException_WhenNotOwner() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        assertThrows(ForbiddenException.class, () -> bookingService.approveBooking(1L, 3L, true));
    }

    @Test
    void approveBooking_ShouldThrowValidationException_WhenNotWaiting() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        assertThrows(ValidationException.class, () -> bookingService.approveBooking(1L, 2L, true));
    }

    @Test
    void getBookingsByBookerIdAndState_ShouldReturnCurrentBookings() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class);
             MockedStatic<LocalDateTime> localDateTime = mockStatic(LocalDateTime.class)) {
            Booking currentBooking = new Booking();
            currentBooking.setId(1L);
            currentBooking.setBooker(booker);
            currentBooking.setItem(item);
            currentBooking.setStartTime(fixedTime.minusHours(1));
            currentBooking.setEndTime(fixedTime.plusHours(1));
            currentBooking.setStatus(BookingStatus.APPROVED);

            localDateTime.when(LocalDateTime::now).thenReturn(fixedTime);
            when(bookingRepository.findCurrentBookingsByBookerId(1L, fixedTime))
                    .thenReturn(Collections.singletonList(currentBooking));
            mapper.when(() -> BookingMapper.convertToDto(any(Booking.class))).thenReturn(bookingDto);

            Collection<BookingDto> result = bookingService.getBookingsByBookerIdAndState(1L, BookingState.CURRENT);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getBookingsByBookerIdAndState_ShouldReturnPastBookings() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class);
             MockedStatic<LocalDateTime> localDateTime = mockStatic(LocalDateTime.class)) {
            Booking pastBooking = new Booking();
            pastBooking.setId(1L);
            pastBooking.setBooker(booker);
            pastBooking.setItem(item);
            pastBooking.setStartTime(fixedTime.minusHours(2));
            pastBooking.setEndTime(fixedTime.minusHours(1));
            pastBooking.setStatus(BookingStatus.APPROVED);

            localDateTime.when(LocalDateTime::now).thenReturn(fixedTime);
            when(bookingRepository.findPastBookingsByBookerId(1L, fixedTime))
                    .thenReturn(Collections.singletonList(pastBooking));
            mapper.when(() -> BookingMapper.convertToDto(any(Booking.class))).thenReturn(bookingDto);

            Collection<BookingDto> result = bookingService.getBookingsByBookerIdAndState(1L, BookingState.PAST);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getBookingsByBookerIdAndState_ShouldReturnFutureBookings() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class);
             MockedStatic<LocalDateTime> localDateTime = mockStatic(LocalDateTime.class)) {
            Booking futureBooking = new Booking();
            futureBooking.setId(1L);
            futureBooking.setBooker(booker);
            futureBooking.setItem(item);
            futureBooking.setStartTime(fixedTime.plusHours(1));
            futureBooking.setEndTime(fixedTime.plusHours(2));
            futureBooking.setStatus(BookingStatus.WAITING);

            localDateTime.when(LocalDateTime::now).thenReturn(fixedTime);
            when(bookingRepository.findFutureBookingsByBookerId(1L, fixedTime))
                    .thenReturn(Collections.singletonList(futureBooking));
            mapper.when(() -> BookingMapper.convertToDto(any(Booking.class))).thenReturn(bookingDto);

            Collection<BookingDto> result = bookingService.getBookingsByBookerIdAndState(1L, BookingState.FUTURE);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getBookingsByBookerIdAndState_ShouldReturnWaitingBookings() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(bookingRepository.findByBooker_UserIdAndStatus(1L, BookingStatus.WAITING))
                    .thenReturn(Collections.singletonList(booking));
            mapper.when(() -> BookingMapper.convertToDto(any(Booking.class))).thenReturn(bookingDto);

            Collection<BookingDto> result = bookingService.getBookingsByBookerIdAndState(1L, BookingState.WAITING);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getBookingsByBookerIdAndState_ShouldReturnRejectedBookings() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            Booking rejectedBooking = new Booking();
            rejectedBooking.setId(1L);
            rejectedBooking.setBooker(booker);
            rejectedBooking.setItem(item);
            rejectedBooking.setStatus(BookingStatus.REJECTED);

            when(bookingRepository.findByBooker_UserIdAndStatus(1L, BookingStatus.REJECTED))
                    .thenReturn(Collections.singletonList(rejectedBooking));
            mapper.when(() -> BookingMapper.convertToDto(any(Booking.class))).thenReturn(bookingDto);

            Collection<BookingDto> result = bookingService.getBookingsByBookerIdAndState(1L, BookingState.REJECTED);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getBookingsByBookerIdAndState_ShouldReturnAllBookings() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(bookingRepository.findByBooker_UserId(1L))
                    .thenReturn(Collections.singletonList(booking));
            mapper.when(() -> BookingMapper.convertToDto(any(Booking.class))).thenReturn(bookingDto);

            Collection<BookingDto> result = bookingService.getBookingsByBookerIdAndState(1L, BookingState.ALL);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getBooking_ShouldReturnBookingForBooker() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            mapper.when(() -> BookingMapper.convertToDto(booking)).thenReturn(bookingDto);

            BookingDto result = bookingService.getBookingById(1L, 1L); // Booker ID

            assertEquals(1L, result.getId());
            assertEquals(bookingDto, result);
        }
    }

    @Test
    void getBooking_ShouldReturnBookingForOwner() {
        try (MockedStatic<BookingMapper> mapper = mockStatic(BookingMapper.class)) {
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            mapper.when(() -> BookingMapper.convertToDto(booking)).thenReturn(bookingDto);

            BookingDto result = bookingService.getBookingById(1L, 2L); // Owner ID

            assertEquals(1L, result.getId());
            assertEquals(bookingDto, result);
        }
    }

    @Test
    void getBooking_ShouldThrowNotFoundException_WhenBookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBooking_ShouldThrowNotFoundException_WhenNotBookerOrOwner() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 3L)); // Neither booker nor owner
    }
}