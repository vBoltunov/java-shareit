package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private ItemRequest request;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() throws Exception {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        if (autoCloseable != null) {
            autoCloseable.close();
        }

        user = new User();
        user.setUserId(1L);

        request = new ItemRequest();
        request.setRequestId(1L);
        request.setUserId(1L);
        request.setDescription("Need a hammer");
        request.setCreated(LocalDateTime.now());

        requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a hammer");
        requestDto.setCreated(request.getCreated());
    }

    @Test
    void addRequest_ShouldAddRequest() {
        try (MockedStatic<ItemRequestMapper> mapper = mockStatic(ItemRequestMapper.class)) {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            mapper.when(() -> ItemRequestMapper.convertToEntity(requestDto, user)).thenReturn(request);
            when(requestRepository.save(request)).thenReturn(request);
            mapper.when(() -> ItemRequestMapper.convertToDto(request)).thenReturn(requestDto);

            ItemRequestDto result = requestService.addRequest(requestDto, 1L);

            assertEquals(1L, result.getId());
            assertEquals("Need a hammer", result.getDescription());
            verify(requestRepository).save(request);
        }
    }

    @Test
    void addRequest_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.addRequest(requestDto, 1L));
    }

    @Test
    void updateRequest_ShouldUpdateRequest() {
        try (MockedStatic<ItemRequestMapper> mapper = mockStatic(ItemRequestMapper.class)) {
            when(userRepository.existsById(1L)).thenReturn(true);
            when(requestRepository.findByUserId(1L)).thenReturn(Collections.singletonList(request));
            mapper.when(() -> ItemRequestMapper.updateRequestFields(request, requestDto)).then(invocation -> {
                request.setDescription(requestDto.getDescription());
                return request;
            });
            when(requestRepository.save(request)).thenReturn(request);
            mapper.when(() -> ItemRequestMapper.convertToDto(request)).thenReturn(requestDto);

            ItemRequestDto result = requestService.updateRequest(requestDto, 1L, 1L);

            assertEquals(1L, result.getId());
            assertEquals("Need a hammer", result.getDescription());
            verify(requestRepository).save(request);
        }
    }

    @Test
    void updateRequest_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> requestService.updateRequest(requestDto, 1L, 1L));
    }

    @Test
    void updateRequest_ShouldThrowNotFoundException_WhenRequestNotFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(requestRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class, () -> requestService.updateRequest(requestDto, 1L, 1L));
    }

    @Test
    void getAllRequestsExceptUser_ShouldReturnRequests() {
        try (MockedStatic<ItemRequestMapper> mapper = mockStatic(ItemRequestMapper.class)) {
            ItemRequest otherRequest = new ItemRequest();
            otherRequest.setRequestId(2L);
            otherRequest.setUserId(2L);
            otherRequest.setDescription("Need a drill");
            ItemRequestDto otherRequestDto = new ItemRequestDto();
            otherRequestDto.setId(2L);
            otherRequestDto.setDescription("Need a drill");
            when(requestRepository.findAll()).thenReturn(Collections.singletonList(otherRequest));
            mapper.when(() -> ItemRequestMapper.convertToDto(otherRequest)).thenReturn(otherRequestDto);

            Collection<ItemRequestDto> result = requestService.getAllRequestsExceptUser(1L);

            assertFalse(result.isEmpty());
            assertEquals(2L, result.iterator().next().getId());
        }
    }

    @Test
    void findByUserId_ShouldReturnRequests() {
        try (MockedStatic<ItemRequestMapper> mapper = mockStatic(ItemRequestMapper.class)) {
            when(requestRepository.findByUserId(1L)).thenReturn(Collections.singletonList(request));
            mapper.when(() -> ItemRequestMapper.convertToDto(request)).thenReturn(requestDto);

            Collection<ItemRequestDto> result = requestService.findByUserId(1L);

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getRequestById_ShouldReturnRequest() {
        try (MockedStatic<ItemRequestMapper> mapper = mockStatic(ItemRequestMapper.class)) {
            when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
            mapper.when(() -> ItemRequestMapper.convertToDto(request)).thenReturn(requestDto);

            ItemRequestDto result = requestService.getRequestById(1L);

            assertEquals(1L, result.getId());
            assertEquals("Need a hammer", result.getDescription());
        }
    }

    @Test
    void getRequestById_ShouldThrowNotFoundException_WhenRequestNotFound() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.getRequestById(1L));
    }

    @Test
    void getRequestById_ShouldThrowNotFoundException_WhenRequestIdNegative() {
        when(requestRepository.findById(-1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.getRequestById(-1L));
    }

    @Test
    void deleteRequest_ShouldDeleteRequest() {
        requestService.deleteRequest(1L, 1L);
        verify(requestRepository).deleteByUserIdAndRequestId(1L, 1L);
    }
}