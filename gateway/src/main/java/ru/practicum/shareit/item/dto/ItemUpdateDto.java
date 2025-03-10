package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents a Data Transfer Object (DTO) for updating an item.
 *
 * This class includes attributes such as the item's id, name, description, availability, owner, and request ID.
 * It uses the `@Getter` annotation to automatically generate getter methods for all fields.
 * It uses the `@NoArgsConstructor` and `@AllArgsConstructor` annotations to generate constructors
 * without arguments and with all arguments, respectively.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to specify custom JSON property names and access levels.
 * It uses the `@NotBlank` and `@NotNull` annotations to validate that certain fields are not null or blank.
 *
 * The `id` field represents the unique identifier of the item, with read-only access.
 * The `name` field represents the name of the item and must not be null or blank.
 * The `description` field represents the description of the item and must not be null or blank.
 * The `available` field represents the availability status of the item and must not be null.
 * The `owner` field represents the unique identifier of the item's owner.
 * The `requestId` field represents the unique identifier of the request associated with the item.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Description is required")
    String description;
    @NotNull(message = "Field 'available' is required")
    Boolean available;
    Long owner;
    Long requestId;
}