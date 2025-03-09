package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private UserDto userDto;

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

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john@example.com");
    }

    @Test
    void getUsers_ShouldReturnUsers() {
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
            mapper.when(() -> UserMapper.convertToDto(user)).thenReturn(userDto);

            var result = userService.getUsers();

            assertFalse(result.isEmpty());
            assertEquals(1L, result.iterator().next().getId());
        }
    }

    @Test
    void getUserById_ShouldReturnUser() {
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            mapper.when(() -> UserMapper.convertToDto(user)).thenReturn(userDto);

            UserDto result = userService.getUserById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }
    }

    @Test
    void getUserById_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void createUser_ShouldCreateUser() {
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            UserDto inputDto = new UserDto();
            inputDto.setName("John");
            inputDto.setEmail("john@example.com");

            when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
            mapper.when(() -> UserMapper.convertToEntity(inputDto)).thenReturn(user);
            when(userRepository.save(user)).thenReturn(user);
            mapper.when(() -> UserMapper.convertToDto(user)).thenReturn(userDto);

            UserDto result = userService.createUser(inputDto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }
    }

    @Test
    void createUser_ShouldThrowIllegalArgumentException_WhenEmailExists() {
        UserDto inputDto = new UserDto();
        inputDto.setName("John");
        inputDto.setEmail("john@example.com");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(inputDto));
    }

    @Test
    void updateUser_ShouldUpdateUser() {
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            long userId = 1L;
            UserDto inputDto = new UserDto();
            inputDto.setName("Updated John");
            inputDto.setEmail("updated@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
            when(userRepository.save(user)).thenReturn(user);
            mapper.when(() -> UserMapper.convertToDto(user)).thenReturn(userDto);

            UserDto result = userService.updateUser(userId, inputDto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(userRepository).save(user);
        }
    }

    @Test
    void updateUser_ShouldThrowNotFoundException_WhenUserNotFound() {
        long userId = 1L;
        UserDto inputDto = new UserDto();
        inputDto.setName("Updated John");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, inputDto));
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldThrowNotFoundException_WhenUserNotFound() {
        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void updateUser_ShouldThrowIllegalArgumentException_WhenEmailExists() {
        long userId = 1L;
        UserDto inputDto = new UserDto();
        inputDto.setEmail("existing@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, inputDto));
    }

    @Test
    void fetchUserById_ShouldThrowIllegalArgumentException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
    }
}