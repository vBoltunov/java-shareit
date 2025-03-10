package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void convertToDto() {
        User user = new User();
        user.setUserId(1L);
        user.setName("John");
        user.setEmail("john@example.com");

        UserDto result = UserMapper.convertToDto(user);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void convertToEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john@example.com");

        User result = UserMapper.convertToEntity(userDto);

        assertEquals(1L, result.getUserId());
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void updateUserFields() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UserDto userDto = new UserDto();
        userDto.setName("New Name");
        userDto.setEmail("new@example.com");

        UserMapper.updateUserFields(user, userDto);

        assertEquals("New Name", user.getName());
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void updateUserProvidedFields() {
        User user = new User();
        user.setUserId(1L);
        user.setName("Old Name");
        user.setEmail("old@example.com");

        UserDto userDto = new UserDto();
        userDto.setName("New Name"); // Email не указан

        UserMapper.updateUserFields(user, userDto);

        assertEquals("New Name", user.getName());
        assertEquals("old@example.com", user.getEmail());
    }
}