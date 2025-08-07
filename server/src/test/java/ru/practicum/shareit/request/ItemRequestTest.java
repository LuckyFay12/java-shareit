package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestTest {

    @Test
    void testItemRequestCreation() {
        User requestor = new User();
        requestor.setId(1L);

        LocalDateTime now = LocalDateTime.now();
        ItemRequest request = ItemRequest.builder()
                .description("Description")
                .requestor(requestor)
                .created(now)
                .build();

        assertNull(request.getId()); // ID генерируется БД
        assertEquals("Description", request.getDescription());
        assertEquals(requestor, request.getRequestor());
        assertEquals(now, request.getCreated());
    }

    @Test
    void testEqualsAndHashCode() {
        User requestor = new User();
        requestor.setId(1L);

        ItemRequest request1 = ItemRequest.builder()
                .id(1L)
                .description("Request 1")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();

        ItemRequest request2 = ItemRequest.builder()
                .id(1L)
                .description("Request 2")
                .requestor(requestor)
                .created(LocalDateTime.now().plusHours(1))
                .build();

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Test request")
                .build();

        assertTrue(request.toString().contains("Test request"));
    }
}

