package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;
    public static final String ITEM_ID_PATH = "/{item-id}";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Fetching all items for user with id: {}", userId);
        return itemService.findByUserId(userId);
    }

    @GetMapping(ITEM_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable("item-id") long itemId) {
        log.info("Fetching item with id: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Adding new item for user with id: {}", userId);
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping(ITEM_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable("item-id") long itemId, @Valid @RequestBody ItemUpdateDto itemDto) {
        log.info("Updating item with id: {} for user with id: {}", itemId, userId);
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @DeleteMapping(ITEM_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("item-id") long itemId) {
        log.info("Deleting item with id: {} for user with id: {}", itemId, userId);
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchItems(@RequestParam("text") String text) {
        log.info("Searching items with text: {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping(ITEM_ID_PATH + "/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("item-id") Long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Adding comment to item with id: {} by user with id: {}", itemId, userId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}