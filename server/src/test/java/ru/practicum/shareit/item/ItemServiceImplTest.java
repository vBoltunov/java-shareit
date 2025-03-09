package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    private Item item;
    private User user;
    private Booking lastBooking;
    private Booking nextBooking;
    private Comment comment;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() throws Exception {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        if (autoCloseable != null) {
            autoCloseable.close();
        }

        user = new User();
        user.setUserId(1L);
        user.setName("John");
        user.setEmail("john@example.com");

        item = new Item();
        item.setItemId(1L);
        item.setName("Hammer");
        item.setDescription("A hammer");
        item.setAvailable(true);
        item.setOwnerId(1L);

        lastBooking = new Booking();
        lastBooking.setId(1L);
        lastBooking.setStartTime(LocalDateTime.now().minusDays(2));
        lastBooking.setEndTime(LocalDateTime.now().minusDays(1));
        lastBooking.setItem(item);
        lastBooking.setBooker(user);
        lastBooking.setStatus(BookingStatus.APPROVED);

        nextBooking = new Booking();
        nextBooking.setId(2L);
        nextBooking.setStartTime(LocalDateTime.now().plusDays(1));
        nextBooking.setEndTime(LocalDateTime.now().plusDays(2));
        nextBooking.setItem(item);
        nextBooking.setBooker(user);
        nextBooking.setStatus(BookingStatus.WAITING);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Hammer");
        itemDto.setDescription("A hammer");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("John");
        commentDto.setCreated(LocalDateTime.now());

        itemRequest = new ItemRequest();
        itemRequest.setRequestId(1L);
    }

    @Test
    void findByUserId_ShouldReturnItems() {
        try (MockedStatic<ItemMapper> mapper = mockStatic(ItemMapper.class)) {
            when(itemRepository.findByOwnerId(1L)).thenReturn(Collections.singletonList(item));
            mapper.when(() -> ItemMapper.convertToDto(item)).thenReturn(itemDto);

            Collection<ItemDto> result = itemService.findByUserId(1L);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getItemById_ShouldReturnItemWithBookingsAndComments() {
        try (MockedStatic<ItemMapper> mapper = mockStatic(ItemMapper.class)) {
            when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            when(bookingRepository.findLastBookingForItem(1L)).thenReturn(lastBooking);
            when(bookingRepository.findNextBookingForItem(1L)).thenReturn(nextBooking);
            when(commentRepository.findByItem_ItemId(1L)).thenReturn(Collections.singletonList(comment));
            mapper.when(() -> ItemMapper.convertToDto(item)).thenReturn(itemDto);

            ItemDto result = itemService.getItemById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(itemRepository).save(item);
        }
    }

    @Test
    void getItemById_ShouldThrowNotFoundException_WhenItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void addItem_ShouldCreateItemWithRequest() {
        try (MockedStatic<ItemMapper> mapper = mockStatic(ItemMapper.class)) {
            long userId = 1L;
            ItemDto inputDto = new ItemDto();
            inputDto.setName("Hammer");
            inputDto.setDescription("A hammer");
            inputDto.setAvailable(true);
            inputDto.setRequestId(1L);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(requestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
            mapper.when(() -> ItemMapper.convertToEntity(inputDto, user, itemRequest)).thenReturn(item);
            when(itemRepository.save(item)).thenReturn(item);
            when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            when(bookingRepository.findLastBookingForItem(1L)).thenReturn(null);
            when(bookingRepository.findNextBookingForItem(1L)).thenReturn(null);
            mapper.when(() -> ItemMapper.convertToDto(item)).thenReturn(itemDto);

            ItemDto result = itemService.addItem(inputDto, userId);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(itemRepository, times(2)).save(item);
        }
    }

    @Test
    void addItem_ShouldThrowNotFoundException_WhenRequestNotFound() {
        long userId = 1L;
        ItemDto inputDto = new ItemDto();
        inputDto.setName("Hammer");
        inputDto.setDescription("A hammer");
        inputDto.setAvailable(true);
        inputDto.setRequestId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addItem(inputDto, userId));
    }

    @Test
    void addItem_ShouldThrowValidationException_WhenAvailableNull() {
        long userId = 1L;
        ItemDto inputDto = new ItemDto();
        inputDto.setName("Hammer");
        inputDto.setDescription("A hammer");
        // available is null

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () -> itemService.addItem(inputDto, userId));
    }

    @Test
    void updateItem_ShouldThrowNotFoundException_WhenItemNotOwned() {
        long userId = 1L;
        long itemId = 1L;
        ItemUpdateDto updateDto = new ItemUpdateDto();
        updateDto.setName("Updated Hammer");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findByOwnerId(userId)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> itemService.updateItem(updateDto, userId, itemId));
    }

    @Test
    void addComment_ShouldAddCommentWithExistingComments() {
        try (MockedStatic<CommentMapper> commentMapper = mockStatic(CommentMapper.class)) {
            long userId = 1L;
            long itemId = 1L;
            CommentDto inputComment = new CommentDto();
            inputComment.setText("Great item!");

            when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(bookingRepository.findByBookerUserIdAndItemItemId(userId, itemId))
                    .thenReturn(Collections.singletonList(lastBooking));
            // Используем изменяемый список
            when(commentRepository.findByItem_ItemId(itemId))
                    .thenReturn(new ArrayList<>(Collections.singletonList(comment)));
            commentMapper.when(() -> CommentMapper.convertToEntity(inputComment, item, user)).thenReturn(comment);
            when(commentRepository.save(comment)).thenReturn(comment);
            commentMapper.when(() -> CommentMapper.convertToDto(comment)).thenReturn(commentDto);

            CommentDto result = itemService.addComment(itemId, userId, inputComment);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Great item!", result.getText());
        }
    }

    // Остальные тесты остаются без изменений
    @Test
    void addItem_ShouldCreateItem() {
        try (MockedStatic<ItemMapper> mapper = mockStatic(ItemMapper.class)) {
            long userId = 1L;
            ItemDto inputDto = new ItemDto();
            inputDto.setName("Hammer");
            inputDto.setDescription("A hammer");
            inputDto.setAvailable(true);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            mapper.when(() -> ItemMapper.convertToEntity(inputDto, user, null)).thenReturn(item);
            when(itemRepository.save(item)).thenReturn(item);
            when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            when(bookingRepository.findLastBookingForItem(1L)).thenReturn(null);
            when(bookingRepository.findNextBookingForItem(1L)).thenReturn(null);
            mapper.when(() -> ItemMapper.convertToDto(item)).thenReturn(itemDto);

            ItemDto result = itemService.addItem(inputDto, userId);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(itemRepository, times(2)).save(item);
        }
    }

    @Test
    void addItem_ShouldThrowNotFoundException_WhenUserNotFound() {
        long userId = 1L;
        ItemDto inputDto = new ItemDto();
        inputDto.setName("Hammer");
        inputDto.setDescription("A hammer");
        inputDto.setAvailable(true);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.addItem(inputDto, userId));
    }

    @Test
    void updateItem_ShouldUpdateItem() {
        try (MockedStatic<ItemMapper> mapper = mockStatic(ItemMapper.class)) {
            long userId = 1L;
            long itemId = 1L;
            ItemUpdateDto updateDto = new ItemUpdateDto();
            updateDto.setName("Updated Hammer");

            when(userRepository.existsById(userId)).thenReturn(true);
            when(itemRepository.findByOwnerId(userId)).thenReturn(Collections.singletonList(item));
            when(itemRepository.save(item)).thenReturn(item);
            when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
            when(bookingRepository.findLastBookingForItem(1L)).thenReturn(null);
            when(bookingRepository.findNextBookingForItem(1L)).thenReturn(null);
            mapper.when(() -> ItemMapper.convertToDto(item)).thenReturn(itemDto);

            ItemDto result = itemService.updateItem(updateDto, userId, itemId);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(itemRepository, times(2)).save(item);
        }
    }

    @Test
    void updateItem_ShouldThrowNotFoundException_WhenUserNotFound() {
        long userId = 1L;
        long itemId = 1L;
        ItemUpdateDto updateDto = new ItemUpdateDto();
        updateDto.setName("Updated Hammer");

        when(userRepository.existsById(userId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemService.updateItem(updateDto, userId, itemId));
    }

    @Test
    void deleteItem_ShouldDeleteItem() {
        long userId = 1L;
        long itemId = 1L;

        itemService.deleteItem(userId, itemId);

        verify(itemRepository).deleteByOwnerIdAndItemId(userId, itemId);
    }

    @Test
    void searchItems_ShouldReturnItems() {
        try (MockedStatic<ItemMapper> mapper = mockStatic(ItemMapper.class)) {
            String text = "hammer";
            when(itemRepository.searchItems(text)).thenReturn(Collections.singletonList(item));
            mapper.when(() -> ItemMapper.convertToDto(item)).thenReturn(itemDto);

            Collection<ItemDto> result = itemService.searchItems(text);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void searchItems_ShouldReturnEmptyList_WhenTextIsEmpty() {
        Collection<ItemDto> result = itemService.searchItems("");

        assertTrue(result.isEmpty());
    }

    @Test
    void addComment_ShouldThrowValidationException_WhenNoApprovedBooking() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto inputComment = new CommentDto();
        inputComment.setText("Great item!");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerUserIdAndItemItemId(userId, itemId))
                .thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> itemService.addComment(itemId, userId, inputComment));
    }

    @Test
    void updateBookingsForItem_ShouldUpdateBookings() {
        long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findLastBookingForItem(itemId)).thenReturn(lastBooking);
        when(bookingRepository.findNextBookingForItem(itemId)).thenReturn(nextBooking);
        when(itemRepository.save(item)).thenReturn(item);

        itemService.updateBookingsForItem(itemId);

        assertEquals(lastBooking, item.getLastBooking());
        assertEquals(nextBooking, item.getNextBooking());
        verify(itemRepository).save(item);
    }
}