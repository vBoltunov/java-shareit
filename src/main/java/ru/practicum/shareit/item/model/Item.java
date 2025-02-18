package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import jakarta.persistence.Transient;
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
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of the itemId field.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the itemId field is only included
 * in the JSON output and cannot be set during deserialization.
 *
 * The `itemId` field represents the unique identifier of the item and is read-only.
 * The `name` field represents the name of the item.
 * The `description` field represents the description of the item.
 * The `isAvailable` field represents the availability status of the item.
 * The `ownerId` field represents the owner of the item.
 * The `request` field represents the request associated with the item (temporarily marked as transient).
 * The `lastBooking` field represents the last booking details of the item.
 * The `nextBooking` field represents the next booking details of the item.
 * The `comments` field represents a collection of comments associated with the item.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long itemId;

    String name;

    String description;

    @Column(name = "is_available")
    boolean isAvailable;

    @Column(name = "owner_id")
    Long ownerId;

    // Временно пометил поле аннотацией Transient, т.к. функционал запросов ещё не реализован
    @Transient
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