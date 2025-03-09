package ru.practicum.shareit.request;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a user request entity with its essential details.
 *
 * This class includes attributes such as the request's id, description, creation timestamp, user id of the requester,
 * and a collection of items associated with this request.
 *
 * It uses the `@Entity` and `@Table` annotations to define it as a JPA entity mapped to the "requests" table.
 * It uses the `@Data` annotation to automatically generate boilerplate code like getters, setters, and constructors.
 * It uses the `@FieldDefaults` annotation to set all fields' access level to `private`.
 *
 * The `requestId` field represents the unique identifier of the request and is generated automatically.
 * The `description` field represents the description of the request.
 * The `created` field represents the timestamp when the request was created.
 * The `userId` field represents the user who made the request.
 * The `items` field represents a collection of items associated with this request.
 *
 * The `@OneToMany` annotation on the `items` field specifies a one-to-many relationship with the `Item` entity,
 * where the `request` field in the `Item` entity is the owning side of the relationship.
 * The cascade type is set to `ALL`, meaning all operations (persist, merge, remove, etc.) will cascade to the `items` collection.
 * The fetch type is set to `LAZY`, meaning the items are loaded on-demand.
 */
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    Long requestId;

    String description;

    LocalDateTime created;

    @Column(name = "user_id")
    Long userId;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Collection<Item> items = new ArrayList<>();
}