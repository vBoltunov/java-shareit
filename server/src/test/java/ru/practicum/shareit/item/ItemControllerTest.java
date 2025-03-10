package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.util.HeaderConstants;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void getItems() throws Exception {
        long userId = 1L;
        when(itemService.findByUserId(userId)).thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items")
                        .header(HeaderConstants.USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getItem() throws Exception {
        long itemId = 1L;
        when(itemService.getItemById(itemId)).thenReturn(itemDto);

        mockMvc.perform(get("/items/{item-id}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void addItem() throws Exception {
        long userId = 1L;
        ItemDto inputDto = new ItemDto();
        inputDto.setName("Hammer");
        inputDto.setDescription("A hammer");
        inputDto.setAvailable(true);

        when(itemService.addItem(any(ItemDto.class), eq(userId))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(HeaderConstants.USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateItem() throws Exception {
        long userId = 1L;
        long itemId = 1L;
        ItemUpdateDto updateDto = new ItemUpdateDto();
        updateDto.setName("Updated Hammer");
        updateDto.setDescription("Updated description");
        updateDto.setAvailable(true);

        when(itemService.updateItem(any(ItemUpdateDto.class), eq(userId), eq(itemId))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{item-id}", itemId)
                        .header(HeaderConstants.USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteItem() throws Exception {
        long userId = 1L;
        long itemId = 1L;

        mockMvc.perform(delete("/items/{item-id}", itemId)
                        .header(HeaderConstants.USER_ID_HEADER, userId))
                .andExpect(status().isNoContent());

        verify(itemService).deleteItem(userId, itemId);
    }

    @Test
    void searchItems() throws Exception {
        String text = "hammer";
        when(itemService.searchItems(text)).thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void addComment() throws Exception {
        long userId = 1L;
        long itemId = 1L;
        CommentDto inputComment = new CommentDto();
        inputComment.setText("Great item!");

        when(itemService.addComment(eq(itemId), eq(userId), any(CommentDto.class))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{item-id}/comment", itemId)
                        .header(HeaderConstants.USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Great item!"));
    }
}