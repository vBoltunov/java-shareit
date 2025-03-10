package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Represents a data transfer object (DTO) for an item request.
 *
 * This class includes attributes such as the request's id, description, creation timestamp,
 * and a collection of associated item DTOs.
 *
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of the `id` field.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the `id` field is only included
 * in the JSON output and cannot be set during deserialization.
 *
 * The `id` field represents the unique identifier of the request and is read-only.
 * The `description` field represents the description of the request.
 * The `created` field represents the timestamp when the request was created.
 * The `items` field represents a collection of item DTOs associated with this request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String description;
    LocalDateTime created;
    Collection<ItemDto> items;
}