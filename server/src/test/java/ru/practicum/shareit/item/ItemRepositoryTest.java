package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Jane");
        owner.setEmail("jane@example.com");
        entityManager.persist(owner);

        item = new Item();
        item.setName("Hammer");
        item.setDescription("A hammer");
        item.setAvailable(true);
        item.setOwnerId(owner.getUserId());
        entityManager.persist(item);

        entityManager.flush();
    }

    @Test
    void findByOwnerId_ShouldReturnItems() {
        Collection<Item> result = itemRepository.findByOwnerId(owner.getUserId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(item.getItemId(), result.iterator().next().getItemId());
    }

    @Test
    void deleteByOwnerIdAndItemId_ShouldDeleteItem() {
        itemRepository.deleteByOwnerIdAndItemId(owner.getUserId(), item.getItemId());

        Item deleted = itemRepository.findById(item.getItemId()).orElse(null);
        assertNull(deleted);
    }

    @Test
    void searchItems_ShouldReturnMatchingItems() {
        Collection<Item> result = itemRepository.searchItems("hammer");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(item.getItemId(), result.iterator().next().getItemId());
    }

    @Test
    void searchItems_ShouldReturnEmpty_WhenNoMatch() {
        Collection<Item> result = itemRepository.searchItems("drill");

        assertTrue(result.isEmpty());
    }
}