package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CommentResponseTest {
    private final LocalDateTime testTime = LocalDateTime.of(2025, 6, 15, 14, 30, 0);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        CommentResponse response = new CommentResponse();

        assertNull(response.getId());
        assertNull(response.getText());
        assertNull(response.getItemName());
        assertNull(response.getAuthorName());
        assertNull(response.getCreated());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        CommentResponse response = new CommentResponse(
                1L,
                "Great item!",
                "Drill",
                "John",
                testTime
        );

        assertEquals(1L, response.getId());
        assertEquals("Great item!", response.getText());
        assertEquals("Drill", response.getItemName());
        assertEquals("John", response.getAuthorName());
        assertEquals(testTime, response.getCreated());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        CommentResponse response = CommentResponse.builder()
                .id(2L)
                .text("perfectly")
                .itemName("Item")
                .authorName("Alice")
                .created(testTime.plusDays(1))
                .build();

        assertEquals(2L, response.getId());
        assertEquals("perfectly", response.getText());
        assertEquals("Item", response.getItemName());
        assertEquals("Alice", response.getAuthorName());
        assertEquals(testTime.plusDays(1), response.getCreated());
    }

    @Test
    void setters_ShouldSetValuesCorrectly() {
        CommentResponse response = new CommentResponse();

        response.setId(3L);
        response.setText("Test comment");
        response.setItemName("Scr");
        response.setAuthorName("Bob");
        response.setCreated(testTime.minusDays(1));

        assertEquals(3L, response.getId());
        assertEquals("Test comment", response.getText());
        assertEquals("Scr", response.getItemName());
        assertEquals("Bob", response.getAuthorName());
        assertEquals(testTime.minusDays(1), response.getCreated());
    }

    @Test
    void toString_ShouldContainAllFields() {
        CommentResponse response = CommentResponse.builder()
                .id(4L)
                .text("ToString test")
                .itemName("Pliers")
                .authorName("Bob")
                .created(testTime)
                .build();

        String str = response.toString();

        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("text=ToString test"));
        assertTrue(str.contains("itemName=Pliers"));
        assertTrue(str.contains("authorName=Bob"));
        assertTrue(str.contains("created=" + testTime));
    }
}