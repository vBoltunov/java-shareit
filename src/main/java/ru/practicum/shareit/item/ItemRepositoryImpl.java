package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    @Override
    public Collection<Item> findByUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(String.valueOf(userId)))
                .toList();
    }

    @Override
    public Optional<Item> findById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item save(Item item) {
        if (item.getItemId() == null) {
            item.setItemId(nextId++);
        }
        items.put(item.getItemId(), item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        items.values().removeIf(
                item -> item.getOwner().equals(String.valueOf(userId)) && item.getItemId() == itemId);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        String lowerCaseText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerCaseText) ||
                        item.getDescription().toLowerCase().contains(lowerCaseText)) && item.isAvailable())
                .toList();
    }
}