package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemRequestMapperTest {

    @Test
    void convertToDto_ShouldMapRequestToDto() {
        User user = new User();
        user.setUserId(1L);
        ItemRequest request = new ItemRequest();
        request.setRequestId(1L);
        request.setDescription("Need a hammer");
        request.setUserId(user.getUserId());
        request.setCreated(LocalDateTime.now());

        ItemRequestDto result = ItemRequestMapper.convertToDto(request);

        assertEquals(1L, result.getId());
        assertEquals("Need a hammer", result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void convertToEntity_ShouldMapDtoToRequest() {
        User user = new User();
        user.setUserId(1L);
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a hammer");

        ItemRequest result = ItemRequestMapper.convertToEntity(dto, user);

        assertEquals("Need a hammer", result.getDescription());
        assertNotNull(result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void updateRequestFields_ShouldUpdateFields() {
        ItemRequest request = new ItemRequest();
        request.setDescription("Old desc");
        request.setCreated(LocalDateTime.now().minusDays(1));

        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("New desc");
        dto.setCreated(LocalDateTime.now());

        ItemRequestMapper.updateRequestFields(request, dto);

        assertEquals("New desc", request.getDescription());
        assertEquals(dto.getCreated(), request.getCreated());
    }
}