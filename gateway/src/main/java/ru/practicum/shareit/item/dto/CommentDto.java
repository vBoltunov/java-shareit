package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Represents a Data Transfer Object (DTO) for a comment.
 *
 * This class includes attributes such as the comment's id, text, author's name, and creation time.
 * It uses the `@Getter` annotation to automatically generate getter methods for all fields.
 * It uses the `@NoArgsConstructor` and `@AllArgsConstructor` annotations to generate constructors
 * without arguments and with all arguments, respectively.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to specify custom JSON property names and access levels.
 *
 * The `id` field represents the unique identifier of the comment, with read-only access.
 * The `text` field represents the content of the comment.
 * The `authorName` field represents the name of the comment's author.
 * The `created` field represents the time when the comment was created, with read-only access.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    @JsonProperty(access = READ_ONLY)
    Long id;
    @NotBlank(message = "Text cannot be empty")
    String text;
    String authorName;
    @JsonProperty(access = READ_ONLY)
    LocalDateTime created;
}
