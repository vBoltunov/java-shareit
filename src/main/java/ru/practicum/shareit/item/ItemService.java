package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findByUserId(long userId);

    ItemDto getItemById(long itemId);

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemUpdateDto itemUpdateDto, long userId, long itemId);

    void deleteItem(long userId, long itemId);

    Collection<ItemDto> searchItems(String text);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);

    void updateBookingsForItem(Long itemId);
}
