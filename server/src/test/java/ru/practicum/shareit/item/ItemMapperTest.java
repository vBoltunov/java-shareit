package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemMapperTest {

    @Test
    void convertToDto() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Hammer");
        item.setDescription("A hammer");
        item.setAvailable(true);
        item.setOwnerId(1L);
        item.setComments(Collections.emptyList());

        ItemDto result = ItemMapper.convertToDto(item);

        assertEquals(1L, result.getId());
        assertEquals("Hammer", result.getName());
        assertEquals("A hammer", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(1L, result.getOwner());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void convertToEntity() {
        User owner = new User();
        owner.setUserId(1L);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Hammer");
        itemDto.setDescription("A hammer");
        itemDto.setAvailable(true);

        Item result = ItemMapper.convertToEntity(itemDto, owner, null);

        assertEquals("Hammer", result.getName());
        assertEquals("A hammer", result.getDescription());
        assertTrue(result.isAvailable());
        assertEquals(1L, result.getOwnerId());
        assertNull(result.getRequest());
    }

    @Test
    void updateItemFields() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Old Hammer");
        item.setDescription("Old desc");
        item.setAvailable(false);

        ItemUpdateDto updateDto = new ItemUpdateDto();
        updateDto.setName("New Hammer");
        updateDto.setDescription("New desc");
        updateDto.setAvailable(true);

        ItemMapper.updateItemFields(item, updateDto);

        assertEquals("New Hammer", item.getName());
        assertEquals("New desc", item.getDescription());
        assertTrue(item.isAvailable());
    }

    @Test
    void updateItemProvidedFields() {
        Item item = new Item();
        item.setItemId(1L);
        item.setName("Old Hammer");
        item.setDescription("Old desc");
        item.setAvailable(false);

        ItemUpdateDto updateDto = new ItemUpdateDto();
        updateDto.setName("New Hammer"); // Остальные поля не указаны

        ItemMapper.updateItemFields(item, updateDto);

        assertEquals("New Hammer", item.getName());
        assertEquals("Old desc", item.getDescription());
        assertFalse(item.isAvailable());
    }
}