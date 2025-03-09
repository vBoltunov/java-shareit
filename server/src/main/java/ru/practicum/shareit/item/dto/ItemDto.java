package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;

import java.util.Collection;

/**
 * Represents a Data Transfer Object (DTO) for an item.
 *
 * This class includes attributes such as the item's id, name, description, availability status, owner id, request id,
 * last booking, next booking, and comments.
 *
 * It uses the `@NotEmpty` annotation to ensure that the item's name is not empty.
 * It uses the `@NotBlank` annotation to ensure that the item's description is not null or blank.
 * It uses the `@NotNull` annotation to ensure that the availability status is provided.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of the `id` field.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the `id` field is only included
 * in the JSON output and cannot be set during deserialization.
 *
 * The `id` field represents the unique identifier of the item, with read-only access.
 * The `name` field represents the name of the item and must not be empty.
 * The `description` field represents the description of the item and must not be null or blank.
 * The `available` field represents the availability status of the item and must not be null.
 * The `owner` field represents the owner of the item.
 * The `requestId` field represents the id of the request associated with the item.
 * The `lastBooking` field represents the last booking details of the item.
 * The `nextBooking` field represents the next booking details of the item.
 * The `comments` field represents a collection of comments associated with the item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    @NotEmpty(message = "Name is required")
    String name;
    @NotBlank(message = "Description is required")
    String description;
    @NotNull(message = "Field 'available' is required")
    Boolean available;
    Long owner;
    Long requestId;
    Booking lastBooking;
    Booking nextBooking;
    Collection<CommentDto> comments;
}