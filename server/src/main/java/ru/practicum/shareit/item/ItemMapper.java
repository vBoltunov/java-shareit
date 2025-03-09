package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto convertToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getItemId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setOwner(item.getOwnerId());
        itemDto.setRequest(item.getRequest());
        itemDto.setComments(item.getComments() != null ?
                item.getComments().stream()
                        .map(CommentMapper::convertToDto)
                        .toList() : Collections.emptyList());
        return itemDto;
    }

    public static Item convertToEntity(ItemDto itemDto, User owner, ItemRequest request) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(owner.getUserId());
        item.setRequest(request);
        item.setComments(itemDto.getComments() != null ?
                itemDto.getComments().stream()
                        .map(commentDto -> CommentMapper.convertToEntity(commentDto, item, owner))
                        .toList() : new ArrayList<>());
        return item;
    }

    public static void updateItemFields(Item item, ItemUpdateDto itemUpdateDto) {
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
        if (itemUpdateDto.getRequest() != null) {
            item.setRequest(itemUpdateDto.getRequest());
        }
    }

}
