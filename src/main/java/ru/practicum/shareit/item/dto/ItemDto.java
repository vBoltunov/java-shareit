package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

/**
 * Represents a Data Transfer Object (DTO) for an item.
 *
 * This class includes attributes such as the item's id, name, description, availability status, owner, and request.
 * It uses the `@NotEmpty` validation annotation to ensure that the item's name is not empty.
 * It uses the `@NotNull` validation annotation to ensure that the item's description and availability status are provided.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of the id field.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the id field is only included
 * in the JSON output and cannot be set during deserialization.
 * The `name` field represents the name of the item and must not be empty.
 * The `description` field represents the description of the item and must be provided.
 * The `available` field represents the availability status of the item and must be provided.
 * The `owner` field represents the owner of the item.
 * The `request` field represents the request associated with the item.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    @NotEmpty(message = "Name is required")
    String name;
    @NotNull(message = "Description is required")
    String description;
    @NotNull(message = "Field 'available' is required")
    Boolean available;
    String owner;
    ItemRequest request;
}