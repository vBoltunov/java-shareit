package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public Collection<ItemDto> findByUserId(long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::convertToDto)
                .toList();
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = fetchItemById(itemId);

        Collection<Comment> comments = commentRepository.findByItem_ItemId(itemId);
        item.setComments(comments);
        log.info("Comments for itemId = {}: {}", itemId, comments.size());

        updateBookingsForItem(itemId);

        return ItemMapper.convertToDto(item);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new NotFoundException(String.format("User with id %s not found", userId));
                });

        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Field 'available' is required");
        }

        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> {
                        log.error("Request with id {} not found", itemDto.getRequestId());
                        return new NotFoundException(String.format("Request with id %s not found", itemDto.getRequestId()));
                    });
        }

        Item item = ItemMapper.convertToEntity(itemDto, owner, request);

        item.setOwnerId(userId);
        item.setAvailable(itemDto.getAvailable());

        Item savedItem = itemRepository.save(item);

        updateBookingsForItem(savedItem.getItemId());

        return ItemMapper.convertToDto(savedItem);
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, long userId, long itemId) {
        log.info("Checking user exists with id: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }

        log.info("Starting update item with id: {}", itemId);
        Item item = itemRepository.findByOwnerId(userId).stream()
                .filter(i -> i.getItemId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %s not found", itemId)));

        log.info("Updating item fields");
        ItemMapper.updateItemFields(item, itemUpdateDto);

        Item updatedItem = itemRepository.save(item);

        updateBookingsForItem(updatedItem.getItemId());
        log.info("Item updated successfully: id = {}", updatedItem.getItemId());

        return ItemMapper.convertToDto(updatedItem);
    }

    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteByOwnerIdAndItemId(userId, itemId);
        log.info("Item deleted successfully: id = {}", itemId);
    }

    public Collection<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::convertToDto)
                .toList();
    }

    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = fetchItemById(itemId);

        User author = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Author with id {} not found", userId);
                    return new NotFoundException(String.format("Author with id %s not found", userId));
                });

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findByBookerUserIdAndItemItemId(userId, itemId);
        log.info("Bookings for userId={}, itemId={}: {}", userId, itemId, bookings);
        log.info("Checking booking for userId={}, itemId={}, current time={}", userId, itemId, now);

        if (bookings.isEmpty()) {
            log.info("No bookings found for userId={} and itemId={}", userId, itemId);
            throw new ValidationException(String.format(
                    "User with id %s has not booked item with id %s", userId, itemId));
        }

        boolean existsApprovedBooking = bookings.stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.APPROVED);
        if (!existsApprovedBooking) {
            throw new ValidationException(String.format(
                    "User with id %s has not booked item with id %s", userId, itemId));
        }

        boolean isCompleted = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .anyMatch(b -> b.getEndTime().isBefore(now));
        if (!isCompleted) {
            throw new ValidationException(String.format(
                    "Cannot comment until booking is completed for userId=%s, itemId=%s", userId, itemId));
        }

        Comment comment = CommentMapper.convertToEntity(commentDto, item, author);

        Collection<Comment> comments = commentRepository.findByItem_ItemId(itemId);
        if (comments == null || comments.isEmpty()) {
            comments = new ArrayList<>();
        }
        item.setComments(comments);

        item.getComments().add(comment);

        Comment savedComment = commentRepository.save(comment);
        log.info("Comment added successfully: id = {}", savedComment.getId());

        return CommentMapper.convertToDto(savedComment);
    }

    public void updateBookingsForItem(Long itemId) {
        Item item = fetchItemById(itemId);

        Booking lastBooking = bookingRepository.findLastBookingForItem(itemId);
        Booking nextBooking = bookingRepository.findNextBookingForItem(itemId);

        log.info("Updating bookings for item with id: {}", itemId);

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);

        itemRepository.save(item);

        log.info("Bookings updated successfully: itemId = {}", itemId);
    }

    private Item fetchItemById(Long itemId) {
        if (itemId == null) {
            log.error("Item id must not be null.");
            throw new IllegalArgumentException("Item id must not be null.");
        }

        Item fetchedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Item with id {} not found", itemId);
                    return new NotFoundException(String.format("Item with id %s not found", itemId));
                });

        log.info("Item fetched successfully: id = {}, name = {}, description = {}",
                fetchedItem.getItemId(), fetchedItem.getName(), fetchedItem.getDescription());

        return fetchedItem;
    }
}