package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private final User testOwner = User.builder().id(1L).name("Owner").build();
    private final ItemRequest testRequest = ItemRequest.builder().id(1L).build();

    @Test
    void testItemCreation() {
        Item item = Item.builder()
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .owner(testOwner)
                .build();

        assertNull(item.getId());
        assertEquals("Дрель", item.getName());
        assertEquals("Аккумуляторная дрель", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(testOwner, item.getOwner());
        assertNull(item.getItemRequest());
    }

    @Test
    void testEqualsAndHashCode() {
        Item item1 = Item.builder()
                .id(1L)
                .name("Item 1")
                .description("Desc 1")
                .available(true)
                .build();

        Item item2 = Item.builder()
                .id(1L)
                .name("Item 2")
                .description("Desc 2")
                .available(false)
                .build();

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());

        Item item3 = Item.builder().id(2L).build();
        assertNotEquals(item1, item3);
    }

    @Test
    void testToString() {
        Item item = Item.builder()
                .id(1L)
                .name("Тестовый предмет")
                .build();

        assertTrue(item.toString().contains("Тестовый предмет"));
    }
}

