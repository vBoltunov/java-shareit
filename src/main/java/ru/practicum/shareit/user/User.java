package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents a user with its essential details.
 *
 * This class includes attributes such as the user's id, name and email.
 * It uses @Email validation annotation to ensure that the user's email is a valid email address.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of the userId field.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the userId field is only included
 * in the JSON output and cannot be set during deserialization.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class User {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long userId;
    String name;
    @Email(message = "Email is not valid")
    String email;
}