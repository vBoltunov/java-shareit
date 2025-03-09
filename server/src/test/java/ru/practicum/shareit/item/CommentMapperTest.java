package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommentMapperTest {

    @Test
    void convertToDto_ShouldMapCommentToDto() {
        User author = new User();
        author.setUserId(1L);
        author.setName("John");
        Item item = new Item();
        item.setItemId(1L);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        CommentDto result = CommentMapper.convertToDto(comment);

        assertEquals(1L, result.getId());
        assertEquals("Great item!", result.getText());
        assertEquals("John", result.getAuthorName());
        assertNotNull(result.getCreated());
    }

    @Test
    void convertToEntity_ShouldMapDtoToComment() {
        User author = new User();
        author.setUserId(1L);
        Item item = new Item();
        item.setItemId(1L);
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");

        Comment result = CommentMapper.convertToEntity(commentDto, item, author);

        assertEquals(1L, result.getId());
        assertEquals("Great item!", result.getText());
        assertEquals(item, result.getItem());
        assertEquals(author, result.getAuthor());
        assertNotNull(result.getCreated());
    }
}