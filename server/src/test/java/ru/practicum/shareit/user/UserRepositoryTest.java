package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        entityManager.persistAndFlush(user);

        boolean exists = userRepository.existsByEmail("john@example.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    void findById_ShouldReturnUser() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        entityManager.persistAndFlush(user);

        User found = userRepository.findById(user.getUserId()).orElse(null);

        assertNotNull(found);
        assertEquals("John", found.getName());
    }
}