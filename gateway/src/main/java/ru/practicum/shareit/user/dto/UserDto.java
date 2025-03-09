package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents a Data Transfer Object (DTO) for a user.
 *
 * This class includes attributes such as the user's id, name, and email.
 * It uses the `@Getter` annotation to automatically generate getter methods for all fields.
 * It uses the `@NoArgsConstructor` and `@AllArgsConstructor` annotations to generate constructors
 * without arguments and with all arguments, respectively.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to specify custom JSON property names and access levels.
 * It uses the `@NotBlank`, `@NotNull`, and `@Email` annotations to validate fields.
 *
 * The `id` field represents the unique identifier of the user, with read-only access.
 * The `name` field represents the name of the user and must not be null or blank.
 * The `email` field represents the email address of the user, which must not be null and must be in a valid format.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotBlank(message = "Name is required")
    String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email is not valid")
    String email;
}
