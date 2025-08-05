package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingShortInfoDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemResponseTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final CommentResponse comment = CommentResponse.builder()
            .id(1L)
            .text("Great item!")
            .authorName("John")
            .created(now)
            .build();

    private final BookingShortInfoDto lastBooking = BookingShortInfoDto.builder()
            .id(1L)
            .start(now.minusDays(2))
            .end(now.minusDays(1))
            .build();

    private final BookingShortInfoDto nextBooking = BookingShortInfoDto.builder()
            .id(2L)
            .start(now.plusDays(1))
            .end(now.plusDays(2))
            .build();

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        ItemResponse response = new ItemResponse();

        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getDescription());
        assertNull(response.getAvailable());
        assertNull(response.getOwnerName());
        assertNull(response.getRequestId());
        assertNull(response.getComments());
        assertNull(response.getLastBooking());
        assertNull(response.getNextBooking());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        ItemResponse response = new ItemResponse(
                1L,
                "Drill",
                "Powerful drill",
                true,
                "Owner",
                10L,
                List.of(comment),
                lastBooking,
                nextBooking
        );

        assertEquals(1L, response.getId());
        assertEquals("Drill", response.getName());
        assertEquals("Powerful drill", response.getDescription());
        assertTrue(response.getAvailable());
        assertEquals("Owner", response.getOwnerName());
        assertEquals(10L, response.getRequestId());
        assertEquals(1, response.getComments().size());
        assertEquals(lastBooking, response.getLastBooking());
        assertEquals(nextBooking, response.getNextBooking());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        ItemResponse response = ItemResponse.builder()
                .id(2L)
                .name("Hammer")
                .description("Steel hammer")
                .available(false)
                .ownerName("Alice")
                .requestId(20L)
                .comments(List.of(comment))
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();

        assertEquals(2L, response.getId());
        assertEquals("Hammer", response.getName());
        assertEquals("Steel hammer", response.getDescription());
        assertFalse(response.getAvailable());
        assertEquals("Alice", response.getOwnerName());
        assertEquals(20L, response.getRequestId());
        assertEquals(1, response.getComments().size());
        assertEquals(lastBooking, response.getLastBooking());
        assertEquals(nextBooking, response.getNextBooking());
    }

    @Test
    void shouldNotBeEqual_WhenFieldsDiffer() {
        ItemResponse response1 = ItemResponse.builder()
                .id(1L)
                .name("Item 1")
                .build();

        ItemResponse response2 = ItemResponse.builder()
                .id(2L)
                .name("Item 2")
                .build();

        assertNotEquals(response1, response2);
    }

    @Test
    void toString_ShouldContainAllFields() {
        ItemResponse response = ItemResponse.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .ownerName("Test Owner")
                .requestId(1L)
                .comments(List.of(comment))
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();

        String str = response.toString();

        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("name=Test Item"));
        assertTrue(str.contains("description=Test Description"));
        assertTrue(str.contains("available=true"));
        assertTrue(str.contains("ownerName=Test Owner"));
        assertTrue(str.contains("requestId=1"));
        assertTrue(str.contains("comments="));
        assertTrue(str.contains("lastBooking="));
        assertTrue(str.contains("nextBooking="));
    }

    @Test
    void shouldHandleNullCollectionsGracefully() {
        ItemResponse response = ItemResponse.builder()
                .id(1L)
                .name("Null Test")
                .comments(null)
                .build();

        assertNull(response.getComments());
    }
}