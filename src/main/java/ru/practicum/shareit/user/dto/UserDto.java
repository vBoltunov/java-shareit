package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Represents a Data Transfer Object (DTO) for a user.
 *
 * This class includes attributes such as the user's id, name, and email.
 * It uses the `@Email` validation annotation to ensure that the user's email is a valid email address.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 *
 * The `id` field represents the unique identifier of the user.
 * The `name` field represents the name of the user.
 * The `email` field represents the email address of the user and is validated to ensure it is a valid email address.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    @Email(message = "Email is not valid")
    String email;
}