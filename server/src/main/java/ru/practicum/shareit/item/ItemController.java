package ru.practicum.shareit.item;

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
import ru.practicum.shareit.util.HeaderConstants;
import ru.practicum.shareit.util.PathConstants;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.ITEMS_PATH)
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> getItems(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId) {
        log.info("Received GET request for all items for user with id: {}", userId);
        return itemService.findByUserId(userId);
    }

    @GetMapping(PathConstants.ITEM_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable("item-id") long itemId) {
        log.info("Received GET request for item with id: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId,
                           @RequestBody ItemDto itemDto) {
        log.info("Received POST request for item for user with id: {}", userId);
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping(PathConstants.ITEM_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId,
                              @PathVariable("item-id") long itemId,
                              @RequestBody ItemUpdateDto itemDto) {
        log.info("Received PATCH request for item with id: {} for user with id: {}", itemId, userId);
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @DeleteMapping(PathConstants.ITEM_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestHeader(HeaderConstants.USER_ID_HEADER) long userId,
                           @PathVariable("item-id") long itemId) {
        log.info("Received DELETE request for item with id: {} for user with id: {}", itemId, userId);
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping(PathConstants.SEARCH_PATH)
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchItems(@RequestParam("text") String text) {
        log.info("Received GET request for items with text: {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping(PathConstants.ITEM_ID_PATH + PathConstants.COMMENT_PATH)
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(@RequestHeader(HeaderConstants.USER_ID_HEADER) Long userId,
                                 @PathVariable("item-id") Long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Received POST request for comment to item with id: {} by user with id: {}", itemId, userId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}