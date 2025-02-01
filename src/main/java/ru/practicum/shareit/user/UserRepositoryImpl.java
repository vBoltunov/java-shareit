package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public Collection<User> getUsers() {
        log.debug("Current number of users: {}", users.size());
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            log.error("User id must not be null.");
            throw new IllegalArgumentException("User id must not be null.");
        }
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new NotFoundException(String.format("User with id %s not found", userId));
                });
    }

    @Override
    public User createUser(User user) {
        if (user.getUserId() == null) {
            user.setUserId(nextId++);
        }
        users.put(user.getUserId(), user);
        log.info("User created successfully: id = {}, name = {}, email = {}",
                user.getUserId(), user.getName(), user.getEmail());
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (userId == null) {
            log.error("Validation failed: id must be provided.");
            throw new ValidationException("User id must be provided");
        }

        User existingUser = users.get(userId);

        if (existingUser == null) {
            log.error("User not found with id: {}", userId);
            throw new NotFoundException("User not found with id: " + userId);
        }

        log.info("Updating user fields in repository");
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        users.put(userId, existingUser);
        log.debug("User updated successfully: id = {}", userId);
        return existingUser;
    }

    @Override
    public void deleteUser(Long userId) {
        if (!users.containsKey(userId)) {
            log.error("User not found: id = {}", userId);
        }
        users.remove(userId);
        log.debug("User deleted successfully: id = {}", userId);
    }

    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}