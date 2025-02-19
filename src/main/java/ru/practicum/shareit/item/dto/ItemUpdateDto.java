package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

/**
 * Represents a Data Transfer Object (DTO) for updating an item, where none of the fields are mandatory.
 *
 * This class includes attributes such as the item's id, name, description, availability status, owner, and request.
 *
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of the id field.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the id field is only included
 * in the JSON output and cannot be set during deserialization.
 *
 * The `id` field represents the unique identifier of the item.
 * The `name` field represents the name of the item and can be updated if provided.
 * The `description` field represents the description of the item and can be updated if provided.
 * The `available` field represents the availability status of the item and can be updated if provided.
 * The `owner` field represents the owner of the item.
 * The `request` field represents the request associated with the item.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    ItemRequest request;
}