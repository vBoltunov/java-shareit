package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Collection<Item> findByUserId(long userId);

    Optional<Item> findById(long itemId);

    Item save(Item item);

    void deleteByUserIdAndItemId(long userId, long itemId);

    Collection<Item> searchItems(String text);
}
