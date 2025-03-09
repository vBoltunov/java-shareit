package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long bookerId) {
        log.info("StartTime: {}, EndTime: {}", bookingDto.getStartTime(), bookingDto.getEndTime());

        if (bookingDto.getStartTime() == null || bookingDto.getEndTime() == null) {
            throw new ValidationException("Booking dates cannot be null");
        }

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> {
                    log.error("Booker with id {} not found", bookerId);
                    return new NotFoundException(String.format("Booker with id %s not found", bookerId));
                });

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Item wit id %s not found", bookingDto.getItemId())));

        if (!item.isAvailable()) {
            throw new ValidationException("Item is not available for booking");
        }

        if (Objects.equals(item.getOwnerId(), bookerId)) {
            throw new NotFoundException("Owner cannot book their own item");
        }

        if (bookingDto.getStartTime().isAfter(bookingDto.getEndTime()) ||
                bookingDto.getStartTime().isEqual(bookingDto.getEndTime())) {
            throw new ValidationException("Invalid booking dates");
        }

        Booking booking = BookingMapper.convertToEntity(bookingDto, item, booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.convertToDto(savedBooking);
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, boolean approved) {
        Booking booking = fetchBookingById(bookingId);

        if (!Objects.equals(booking.getItem().getOwnerId(), ownerId)) {
            throw new ForbiddenException("Only the owner can approve the booking");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Booking status is not WAITING");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);

        return BookingMapper.convertToDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = fetchBookingById(bookingId);

        if (!booking.getBooker().getUserId().equals(userId) &&
                !Objects.equals(booking.getItem().getOwnerId(), userId)) {
            throw new NotFoundException("Only the booker or owner can view the booking");
        }

        return BookingMapper.convertToDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingsByOwnerId(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException(String.format("User with id %s not found: ", ownerId));
        }

        return bookingRepository.findByItem_OwnerId(ownerId).stream()
                .map(BookingMapper::convertToDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getBookingsByBookerIdAndState(Long bookerId, BookingState state) {
        Collection<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findCurrentBookingsByBookerId(bookerId, LocalDateTime.now());
            case PAST -> bookingRepository.findPastBookingsByBookerId(bookerId, LocalDateTime.now());
            case FUTURE -> bookingRepository.findFutureBookingsByBookerId(bookerId, LocalDateTime.now());
            case WAITING -> bookingRepository.findByBooker_UserIdAndStatus(bookerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByBooker_UserIdAndStatus(bookerId, BookingStatus.REJECTED);
            default -> bookingRepository.findByBooker_UserId(bookerId);
        };

        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStartTime).reversed())
                .map(BookingMapper::convertToDto)
                .toList();
    }

    private Booking fetchBookingById(Long id) {
        if (id == null) {
            log.error("Booking id must not be null.");
            throw new IllegalArgumentException("Booking id must not be null.");
        }

        Booking fetchedBooking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Booking with id {} not found", id);
                    return new NotFoundException(String.format("Booking with id %s not found", id));
                });

        log.info("Booking fetched successfully: id = {}", fetchedBooking.getId());

        return fetchedBooking;
    }
}