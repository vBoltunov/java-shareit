package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class BookingRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private User booker;
    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        booker = new User();
        booker.setName("John");
        booker.setEmail("john@example.com");
        entityManager.persist(booker);

        owner = new User();
        owner.setName("Jane");
        owner.setEmail("jane@example.com");
        entityManager.persist(owner);

        item = new Item();
        item.setName("Hammer");
        item.setDescription("A hammer");
        item.setAvailable(true);
        item.setOwnerId(owner.getUserId());
        entityManager.persist(item);

        booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStartTime(LocalDateTime.now().minusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        booking.setStatus(BookingStatus.APPROVED);
        entityManager.persist(booking);

        entityManager.flush();
    }

    @Test
    void findByBookerId() {
        Collection<Booking> result = bookingRepository.findByBooker_UserId(booker.getUserId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.iterator().next().getId());
    }

    @Test
    void findByItemOwnerId() {
        Collection<Booking> result = bookingRepository.findByItem_OwnerId(owner.getUserId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.iterator().next().getId());
    }

    @Test
    void findByBookerIdAndStatus() {
        Collection<Booking> result = bookingRepository.findByBooker_UserIdAndStatus(booker.getUserId(), BookingStatus.APPROVED);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.iterator().next().getId());
    }

    @Test
    void findCurrentBookingsByBookerId() {
        Collection<Booking> result = bookingRepository.findCurrentBookingsByBookerId(booker.getUserId(), LocalDateTime.now());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.iterator().next().getId());
    }

    @Test
    void findPastBookingsByBookerId() {
        Booking pastBooking = new Booking();
        pastBooking.setBooker(booker);
        pastBooking.setItem(item);
        pastBooking.setStartTime(LocalDateTime.now().minusDays(2));
        pastBooking.setEndTime(LocalDateTime.now().minusDays(1));
        pastBooking.setStatus(BookingStatus.APPROVED);
        entityManager.persist(pastBooking);
        entityManager.flush();

        Collection<Booking> result = bookingRepository.findPastBookingsByBookerId(booker.getUserId(), LocalDateTime.now());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.iterator().next().getId());
    }

    @Test
    void findFutureBookingsByBookerId() {
        Booking futureBooking = new Booking();
        futureBooking.setBooker(booker);
        futureBooking.setItem(item);
        futureBooking.setStartTime(LocalDateTime.now().plusDays(1));
        futureBooking.setEndTime(LocalDateTime.now().plusDays(2));
        futureBooking.setStatus(BookingStatus.APPROVED);
        entityManager.persist(futureBooking);
        entityManager.flush();

        Collection<Booking> result = bookingRepository.findFutureBookingsByBookerId(booker.getUserId(), LocalDateTime.now());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(futureBooking.getId(), result.iterator().next().getId());
    }

    @Test
    void findLastBookingForItem() {
        Booking pastBooking = new Booking();
        pastBooking.setBooker(booker);
        pastBooking.setItem(item);
        pastBooking.setStartTime(LocalDateTime.now().minusDays(2));
        pastBooking.setEndTime(LocalDateTime.now().minusDays(1));
        pastBooking.setStatus(BookingStatus.APPROVED);
        entityManager.persist(pastBooking);
        entityManager.flush();

        Booking result = bookingRepository.findLastBookingForItem(item.getItemId());

        assertNotNull(result);
        assertEquals(pastBooking.getId(), result.getId());
    }

    @Test
    @DirtiesContext
    @Transactional
    void findNextBookingForItem() {
        entityManager.createQuery("DELETE FROM Booking b WHERE b.item.itemId = :itemId")
                .setParameter("itemId", item.getItemId())
                .executeUpdate();
        entityManager.flush();
        entityManager.clear(); // Очищаем кэш первого уровня, чтобы исключить старые данные

        Booking futureBooking = new Booking();
        futureBooking.setBooker(booker);
        futureBooking.setItem(item);
        futureBooking.setStartTime(LocalDateTime.now().plusDays(1));
        futureBooking.setEndTime(LocalDateTime.now().plusDays(2));
        futureBooking.setStatus(BookingStatus.APPROVED);
        entityManager.persist(futureBooking);
        entityManager.flush();

        List<Booking> allBookings = entityManager.createQuery(
                "SELECT b FROM Booking b WHERE b.item.itemId = :itemId", Booking.class)
                .setParameter("itemId", item.getItemId())
                .getResultList();
        System.out.println("All bookings after setup: " + allBookings);

        Booking result = bookingRepository.findNextBookingForItem(item.getItemId());

        assertNotNull(result);
        assertEquals(futureBooking.getId(), result.getId());
    }

    @Test
    void findByBookerIdAndItemId() {
        List<Booking> result = bookingRepository.findByBookerUserIdAndItemItemId(booker.getUserId(), item.getItemId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.getFirst().getId());
    }
}