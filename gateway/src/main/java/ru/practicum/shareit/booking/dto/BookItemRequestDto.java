package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Represents a Data Transfer Object (DTO) for an item booking request.
 *
 * This class includes attributes such as the item's id, start time, and end time.
 * It uses the `@Getter` annotation to automatically generate getter methods for all fields.
 * It uses the `@NoArgsConstructor` and `@AllArgsConstructor` annotations to generate constructors without arguments and with all arguments, respectively.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 *
 * The `itemId` field represents the unique identifier of the item being booked.
 * The `start` field represents the start time of the booking, which must be in the present or future.
 * The `end` field represents the end time of the booking, which must be in the future.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookItemRequestDto {
	@Positive
	long itemId;
	@NotNull(message = "Start time cannot be null")
	@FutureOrPresent(message = "Start time must be in the present or future")
	LocalDateTime start;
	@NotNull(message = "End time cannot be null")
	@Future(message = "End time must be in the future")
	LocalDateTime end;
}
