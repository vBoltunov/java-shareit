package ru.practicum.shareit.item.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * Represents a comment entity with its essential details.
 *
 * This class includes attributes such as the comment's id, text, associated item, author, and creation timestamp.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 *
 * The `id` field represents the unique identifier of the comment.
 * The `text` field represents the text content of the comment.
 * The `item` field represents the item associated with the comment.
 * The `author` field represents the author of the comment.
 * The `created` field represents the timestamp when the comment was created.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String text;

    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    LocalDateTime created;
}