package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Represents a Data Transfer Object (DTO) for a comment.
 *
 * This class includes attributes such as the comment's id, text, author name, and creation timestamp.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 *
 * The `id` field represents the unique identifier of the comment.
 * The `text` field represents the text content of the comment.
 * The `authorName` field represents the name of the author of the comment.
 * The `created` field represents the timestamp when the comment was created.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}