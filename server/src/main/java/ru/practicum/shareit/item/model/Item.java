package ru.practicum.shareit.item.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents an item entity with its essential details.
 *
 * This class includes attributes such as the item's id, name, description, availability status, owner id,
 * a link to the corresponding user request (if the item was created at the request of another user),
 * last booking, next booking, and a collection of comments.
 *
 * It uses the `@Entity` and `@Table` annotations to define it as a JPA entity mapped to the "items" table.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@NotBlank` and `@NotNull` annotations to ensure that certain fields are provided and not blank.
 *
 * The `itemId` field represents the unique identifier of the item, automatically generated.
 * The `name` field represents the name of the item and must not be null or blank.
 * The `description` field represents the description of the item and must not be null or blank.
 * The `isAvailable` field represents the availability status of the item and must not be null.
 * The `ownerId` field represents the owner of the item.
 * The `request` field represents the request associated with the item.
 * The `lastBooking` field represents the last booking details of the item.
 * The `nextBooking` field represents the next booking details of the item.
 * The `comments` field represents a collection of comments associated with the item.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long itemId;

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Description is required")
    String description;

    @NotNull(message = "Field 'available' is required")
    @Column(name = "is_available")
    boolean isAvailable;

    @Column(name = "owner_id")
    Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    ItemRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_booking_id")
    Booking lastBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_booking_id")
    Booking nextBooking;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Collection<Comment> comments = new ArrayList<>();
}