package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents a user entity with its essential details.
 *
 * This class includes attributes such as the user's id, name, and email.
 * It uses the `@Entity` and `@Table` annotations to define it as a JPA entity mapped to the "users" table.
 * It uses the `@Email` validation annotation to ensure that the user's email is a valid email address.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@JsonProperty` annotation to control the serialization and deserialization of the userId field.
 * The `access = JsonProperty.Access.READ_ONLY` parameter ensures that the userId field is only included
 * in the JSON output and cannot be set during deserialization.
 *
 * The `userId` field represents the unique identifier of the user, with read-only access and automatically generated.
 * The `name` field represents the name of the user.
 * The `email` field represents the email address of the user and must be a valid email address.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    String name;

    @Email(message = "Email is not valid")
    String email;
}