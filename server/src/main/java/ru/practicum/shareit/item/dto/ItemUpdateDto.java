package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Represents a Data Transfer Object (DTO) for updating an item.
 *
 * This class includes attributes such as the item's id, name, description, availability status, owner, and request.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of fields.
 * It uses the `@NotBlank` and `@NotNull` annotations to ensure that certain fields are provided and not blank.
 *
 * The `id` field represents the unique identifier of the item, with read-only access.
 * The `name` field represents the name of the item and must not be null or blank.
 * The `description` field represents the description of the item and must not be null or blank.
 * The `available` field represents the availability status of the item and must not be null.
 * The `owner` field represents the id of the owner of the item.
 * The `request` field represents the request associated with the item, with read-only access.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateDto {
    @JsonProperty(access = READ_ONLY)
    Long id;
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Description is required")
    String description;
    @NotNull(message = "Field 'available' is required")
    Boolean available;
    Long owner;
    @JsonProperty(access = READ_ONLY)
    ItemRequest request;
}