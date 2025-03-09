package ru.practicum.shareit.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * Represents a booking entity with its essential details.
 *
 * This class includes attributes such as the booking's id, start time, end time, item, booker, and status.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 *
 * The `id` field represents the unique identifier of the booking.
 * The `startTime` field represents the start time of the booking.
 * The `endTime` field represents the end time of the booking.
 * The `item` field represents the item being booked.
 * The `booker` field represents the user who booked the item.
 * The `status` field represents the current status of the booking.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;

    @Enumerated(EnumType.STRING)
    BookingStatus status;
}