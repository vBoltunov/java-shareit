package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByBooker_UserId(Long bookerId);

    Collection<Booking> findByItem_OwnerId(Long ownerId);

    Collection<Booking> findByBooker_UserIdAndStatus(Long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.booker.userId = :bookerId AND b.startTime <= :now AND b.endTime >= :now ORDER BY b.startTime DESC")
    Collection<Booking> findCurrentBookingsByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.userId = :bookerId AND b.endTime < :now ORDER BY b.startTime DESC")
    Collection<Booking> findPastBookingsByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.userId = :bookerId AND b.startTime > :now ORDER BY b.startTime DESC")
    Collection<Booking> findFutureBookingsByBookerId(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.itemId = :itemId AND b.endTime < CURRENT_TIMESTAMP ORDER BY b.endTime DESC")
    Booking findLastBookingForItem(@Param("itemId") Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.itemId = :itemId AND b.startTime > CURRENT_TIMESTAMP ORDER BY b.startTime ASC LIMIT 1")
    Booking findNextBookingForItem(@Param("itemId") Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.booker.userId = :bookerId AND b.item.itemId = :itemId")
    List<Booking> findByBookerUserIdAndItemItemId(@Param("bookerId") Long bookerId, @Param("itemId") Long itemId);
}