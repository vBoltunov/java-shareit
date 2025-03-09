package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Represents a Data Transfer Object (DTO) for an item request.
 *
 * This class includes attributes such as the request's id, description, creation time, and a collection of items.
 * It uses the `@Getter` annotation to automatically generate getter methods for all fields.
 * It uses the `@NoArgsConstructor` and `@AllArgsConstructor` annotations to generate constructors without arguments and with all arguments, respectively.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to specify custom JSON property names and access levels.
 * It uses the `@NotBlank` annotation to validate that the description is not null or blank.
 *
 * The `id` field represents the unique identifier of the item request, with read-only access.
 * The `description` field represents the description of the item request and must not be null or blank.
 * The `created` field represents the time when the item request was created.
 * The `items` field represents a collection of items associated with the item request.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    @NotBlank(message = "Description is required")
    String description;
    LocalDateTime created;
    Collection<ItemDto> items;
}
