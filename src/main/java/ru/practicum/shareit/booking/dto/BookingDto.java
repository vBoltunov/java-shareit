package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * Represents a Data Transfer Object (DTO) for a booking.
 *
 * This class includes attributes such as the booking's id, start time, end time, itemId, status, booker, and item.
 * It uses the `@JsonProperty` annotation to specify custom JSON property names and access levels.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 *
 * The `id` field represents the unique identifier of the booking.
 * The `startTime` field represents the start time of the booking.
 * The `endTime` field represents the end time of the booking.
 * The `itemId` field represents the unique identifier of the item being booked, with write-only access.
 * The `status` field represents the current status of the booking.
 * The `booker` field represents the user who booked the item.
 * The `item` field represents the item being booked.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;

    @JsonProperty("start")
    LocalDateTime startTime;

    @JsonProperty("end")
    LocalDateTime endTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long itemId;

    BookingStatus status;
    UserDto booker;
    ItemDto item;
}