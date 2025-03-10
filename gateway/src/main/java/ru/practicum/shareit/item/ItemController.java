package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.util.HeaderConstants;
import ru.practicum.shareit.util.PathConstants;

@Controller
@RequestMapping(path = PathConstants.ITEMS_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId) {
        log.info("Sending GET request for all items for user with id: {}", userId);
        return itemClient.findByUserId(userId);
    }

    @GetMapping(PathConstants.ITEM_ID_PATH)
    public ResponseEntity<Object> getItem(@PathVariable("item-id") @Positive long itemId) {
        log.info("Sending GET request for item with id: {}", itemId);
        return itemClient.getItemById(itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
                                           @RequestBody @Valid ItemDto itemDto) {
        log.info("Sending POST request for item {} for userId {}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping(PathConstants.ITEM_ID_PATH)
    public ResponseEntity<Object> updateItem(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
                              @PathVariable("item-id") @Positive long itemId, @RequestBody @Valid ItemUpdateDto itemDto) {
        log.info("Sending PATCH request for item with id: {} for user with id: {}", itemId, userId);
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @DeleteMapping(PathConstants.ITEM_ID_PATH)
    public ResponseEntity<Object> deleteItem(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive long userId,
                           @PathVariable("item-id") @Positive long itemId) {
        log.info("Sending DELETE request for item with id: {} for user with id: {}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping(PathConstants.SEARCH_PATH)
    public ResponseEntity<Object> searchItems(@RequestParam("text") @NotNull String text) {
        log.info("Sending GET request for items with text: {}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping(PathConstants.ITEM_ID_PATH + PathConstants.COMMENT_PATH)
    public ResponseEntity<Object> addComment(@RequestHeader(HeaderConstants.USER_ID_HEADER) @Positive Long userId,
                                 @PathVariable("item-id") @Positive Long itemId,
                                 @RequestBody @Valid CommentDto commentDto) {
        log.info("Sending POST request for comment to item with id: {} by user with id: {}", itemId, userId);
        return itemClient.addComment(itemId, userId, commentDto);
    }
}
