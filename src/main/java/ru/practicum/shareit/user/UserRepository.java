package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserRepository {
    Collection<User> getUsers();

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    boolean existsByEmail(String email);
}