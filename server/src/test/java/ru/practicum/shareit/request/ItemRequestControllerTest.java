package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.util.HeaderConstants;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService requestService;

    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Hammer");
        itemDto.setDescription("A sturdy hammer");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
        itemDto.setRequestId(1L);

        requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a hammer");
        requestDto.setCreated(LocalDateTime.now());
        requestDto.setItems(Collections.singletonList(itemDto));
    }

    @Test
    void getRequests() throws Exception {
        long userId = 1L;
        when(requestService.findByUserId(userId)).thenReturn(Collections.singletonList(requestDto));

        mockMvc.perform(get("/requests")
                        .header(HeaderConstants.USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Need a hammer"))
                .andExpect(jsonPath("$[0].items[0].id").value(1L))
                .andExpect(jsonPath("$[0].items[0].name").value("Hammer"));
    }

    @Test
    void getRequest() throws Exception {
        long requestId = 1L;
        when(requestService.getRequestById(requestId)).thenReturn(requestDto);

        mockMvc.perform(get("/requests/{request-id}", requestId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a hammer"))
                .andExpect(jsonPath("$.items[0].id").value(1L));
    }

    @Test
    void addRequest() throws Exception {
        long userId = 1L;
        ItemRequestDto inputDto = new ItemRequestDto();
        inputDto.setDescription("Need a hammer");
        when(requestService.addRequest(any(ItemRequestDto.class), eq(userId))).thenReturn(requestDto);

        String jsonRequest = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(post("/requests")
                        .header(HeaderConstants.USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a hammer"))
                .andExpect(jsonPath("$.items[0].name").value("Hammer"));
    }

    @Test
    void updateRequest() throws Exception {
        long userId = 1L;
        long requestId = 1L;
        ItemRequestDto inputDto = new ItemRequestDto();
        inputDto.setDescription("Updated request");
        when(requestService.updateRequest(any(ItemRequestDto.class), eq(userId), eq(requestId))).thenReturn(requestDto);

        String jsonRequest = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(patch("/requests/{request-id}", requestId)
                        .header(HeaderConstants.USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Need a hammer")); // Возвращается мок
    }

    @Test
    void deleteRequest() throws Exception {
        long userId = 1L;
        long requestId = 1L;

        mockMvc.perform(delete("/requests/{request-id}", requestId)
                        .header(HeaderConstants.USER_ID_HEADER, userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllRequests() throws Exception {
        long userId = 1L;
        when(requestService.getAllRequestsExceptUser(userId)).thenReturn(Collections.singletonList(requestDto));

        mockMvc.perform(get("/requests/all")
                        .header(HeaderConstants.USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Need a hammer"));
    }
}