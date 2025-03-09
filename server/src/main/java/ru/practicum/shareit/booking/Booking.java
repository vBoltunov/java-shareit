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
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * Represents a booking entity with its essential details.
 *
 * This class includes attributes such as the booking's id, start time, end time, item, booker, and status.
 * It uses the `@Entity` and `@Table` annotations to define it as a JPA entity mapped to the "bookings" table.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@NotNull` annotation to ensure that certain fields are provided.
 *
 * The `id` field represents the unique identifier of the booking, automatically generated.
 * The `startTime` field represents the start time of the booking and must not be null.
 * The `endTime` field represents the end time of the booking and must not be null.
 * The `item` field represents the item being booked and must not be null.
 * The `booker` field represents the user who booked the item and must not be null.
 * The `status` field represents the current status of the booking and must not be null.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "start_time")
    LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time")
    LocalDateTime endTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;

    @NotNull
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}