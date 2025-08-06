package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserCreation() {
        User user = User.builder()
                .name("Ivan")
                .email("ivan@example.com")
                .build();

        assertNull(user.getId());
        assertEquals("Ivan", user.getName());
        assertEquals("ivan@example.com", user.getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = User.builder()
                .id(1L)
                .name("User 1")
                .email("user1@example.com")
                .build();

        User user2 = User.builder()
                .id(1L)
                .name("User 2")
                .email("user2@example.com")
                .build();

        assertEquals(user1, user2, "Пользователи с одинаковым ID должны быть равны");
        assertEquals(user1.hashCode(), user2.hashCode(),
                "Хэш-коды должны совпадать для одинаковых ID");

        User user3 = User.builder()
                .id(2L)
                .name("User 1")
                .email("user1@example.com")
                .build();

        assertNotEquals(user1, user3, "Пользователи с разными ID не должны быть равны");
    }

    @Test
    void testToString() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        String toStringResult = user.toString();
        assertTrue(toStringResult.contains("Test User"));
        assertTrue(toStringResult.contains("test@example.com"));
    }
}


