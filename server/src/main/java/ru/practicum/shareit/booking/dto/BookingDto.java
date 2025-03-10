package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * Represents a Data Transfer Object (DTO) for a booking.
 *
 * This class includes attributes such as the booking's id, start time, end time, item ID, status, booker, and item.
 * It uses the `@JsonProperty` annotation to specify custom JSON property names and access levels.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@NotNull` annotation to ensure that certain fields are provided.
 *
 * The `id` field represents the unique identifier of the booking.
 * The `startTime` field represents the start time of the booking and must not be null.
 * The `endTime` field represents the end time of the booking and must not be null.
 * The `itemId` field represents the unique identifier of the item being booked, with write-only access, and must not be null.
 * The `status` field represents the current status of the booking.
 * The `booker` field represents the user who booked the item.
 * The `item` field represents the item being booked.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;

    @NotNull(message = "Start time cannot be null")
    @JsonProperty("start")
    LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @JsonProperty("end")
    LocalDateTime endTime;

    @NotNull(message = "Item ID cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long itemId;

    BookingStatus status;
    UserDto booker;
    ItemDto item;
}