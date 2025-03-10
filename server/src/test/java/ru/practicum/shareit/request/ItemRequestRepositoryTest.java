package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        entityManager.persist(user);

        itemRequest = new ItemRequest();
        itemRequest.setDescription("Need a hammer");
        itemRequest.setUserId(user.getUserId());
        itemRequest.setCreated(LocalDateTime.now());
        entityManager.persist(itemRequest);

        entityManager.flush();
    }

    @Test
    void findRequestsByUserId() {
        Collection<ItemRequest> result = itemRequestRepository.findByUserId(user.getUserId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(itemRequest.getRequestId(), result.iterator().next().getRequestId());
    }

    @Test
    void deleteByUserIdAndRequestId() {
        itemRequestRepository.deleteByUserIdAndRequestId(user.getUserId(), itemRequest.getRequestId());

        ItemRequest deleted = itemRequestRepository.findById(itemRequest.getRequestId()).orElse(null);
        assertNull(deleted);
    }
}