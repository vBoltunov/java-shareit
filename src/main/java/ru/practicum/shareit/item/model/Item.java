package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;

/**
 * Represents an item with its essential details.
 *
 * This class includes attributes such as the item's id, name, description, availability status, owner name, and
 * a link to the corresponding user request (if the item was created at the request of another user).
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the itemId field is only included
 * in the JSON output and cannot be set during deserialization.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long itemId;
    String name;
    String description;
    boolean available;
    String owner;
    ItemRequest request;
}