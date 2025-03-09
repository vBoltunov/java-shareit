package ru.practicum.shareit.item.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * Represents a comment entity with its essential details.
 *
 * This class includes attributes such as the comment's id, text, associated item, author, and creation timestamp.
 * It uses the `@Entity` and `@Table` annotations to define it as a JPA entity mapped to the "comments" table.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 * It uses the `@NotBlank` and `@NotNull` annotations to ensure that certain fields are provided and not blank.
 *
 * The `id` field represents the unique identifier of the comment, automatically generated.
 * The `text` field represents the text content of the comment and must not be null or blank.
 * The `item` field represents the item associated with the comment and must not be null.
 * The `author` field represents the author of the comment and must not be null.
 * The `created` field represents the timestamp when the comment was created and must not be null.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @NotBlank(message = "Comment text cannot be empty")
    String text;

    @NotNull(message = "Item cannot be null")
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @NotNull(message = "Author cannot be null")
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @Column(nullable = false)
    @NotNull(message = "Created time cannot be null")
    LocalDateTime created;
}