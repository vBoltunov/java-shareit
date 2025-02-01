package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Collection<ItemDto> findByUserId(long userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(ItemMapper::convertToDto)
                .toList();
    }

    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        log.info("Item fetched successfully: id = {}, name = {}, description = {}",
                item.getItemId(), item.getName(), item.getDescription());
        return ItemMapper.convertToDto(item);
    }

    public ItemDto addItem(ItemDto itemDto, long userId) {
        userRepository.getUserById(userId);

        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Field 'available' is required");
        }

        Item item = ItemMapper.convertToEntity(itemDto);
        item.setOwner(String.valueOf(userId));
        item.setAvailable(itemDto.getAvailable()); // Устанавливаем значение поля available
        Item savedItem = itemRepository.save(item);
        return ItemMapper.convertToDto(savedItem);
    }

    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, long userId, long itemId) {
        userRepository.getUserById(userId);

        log.info("Starting update item with id: {}", itemId);
        Item item = itemRepository.findByUserId(userId).stream()
                .filter(i -> i.getItemId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found"));

        log.info("Updating item fields");
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
        item.setRequest(itemUpdateDto.getRequest());

        Item updatedItem = itemRepository.save(item);

        log.info("Item updated successfully: id = {}", updatedItem.getItemId());
        return ItemMapper.convertToDto(updatedItem);
    }

    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByUserIdAndItemId(userId, itemId);
        log.info("Item deleted successfully: id = {}", itemId);
    }

    public Collection<ItemDto> searchItems(String text) {
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::convertToDto)
                .toList();
    }
}
