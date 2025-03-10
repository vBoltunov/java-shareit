package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Represents a Data Transfer Object (DTO) for a comment.
 *
 * This class includes attributes such as the comment's id, text, author name, and creation timestamp.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to specify custom JSON property access levels.
 * It uses the `@NotBlank` annotation to ensure the text field is not null or blank.
 *
 * The `id` field represents the unique identifier of the comment, with read-only access.
 * The `text` field represents the text content of the comment and must not be null or blank.
 * The `authorName` field represents the name of the author of the comment.
 * The `created` field represents the timestamp when the comment was created, with read-only access.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    @JsonProperty(access = READ_ONLY)
    Long id;
    @NotBlank(message = "Comment text is required")
    String text;
    String authorName;
    @JsonProperty(access = READ_ONLY)
    LocalDateTime created;
}