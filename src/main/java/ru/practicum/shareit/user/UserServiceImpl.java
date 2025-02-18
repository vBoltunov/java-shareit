package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;

    public Collection<UserDto> getUsers() {
        return userRepository.getUsers().stream()
                .map(UserMapper::convertToDto)
                .toList();
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.getUserById(userId);
        log.info("User fetched successfully: id = {}, name = {}, email = {}",
                user.getUserId(), user.getName(), user.getEmail());
        return UserMapper.convertToDto(user);
    }

    public UserDto createUser(UserDto userDto) {
        validateEmail(userDto.getEmail());
        User user = UserMapper.convertToEntity(userDto);
        User createdUser = userRepository.createUser(user);
        return UserMapper.convertToDto(createdUser);
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Starting updateUser with id: {}", userId);
        User existingUser = userRepository.getUserById(userId);
        if (existingUser == null) {
            log.error("User not found with id: {}", userId);
            throw new NotFoundException("User not found with id: " + userId);
        }

        if (userDto.getEmail() != null &&
                (existingUser.getEmail() == null || !existingUser.getEmail().equals(userDto.getEmail()))) {
            log.info("Validating email: {}", userDto.getEmail());
            validateEmail(userDto.getEmail());
        }

        log.info("Updating user fields");
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        User updatedUser = userRepository.updateUser(userId, existingUser);

        log.info("User updated successfully: id = {}", updatedUser.getUserId());
        return UserMapper.convertToDto(updatedUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must be provided");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use by another user");
        }
    }
}