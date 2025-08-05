package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentCreateRequestTest {

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        CommentCreateRequest request = new CommentCreateRequest();

        assertNull(request.getId());
        assertNull(request.getText());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        CommentCreateRequest request = new CommentCreateRequest(1L, "Test comment");

        assertEquals(1L, request.getId());
        assertEquals("Test comment", request.getText());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        CommentCreateRequest request = CommentCreateRequest.builder()
                .id(2L)
                .text("Builder comment")
                .build();

        assertEquals(2L, request.getId());
        assertEquals("Builder comment", request.getText());
    }

    @Test
    void setters_ShouldSetValuesCorrectly() {
        CommentCreateRequest request = new CommentCreateRequest();

        request.setId(3L);
        request.setText("Setter comment");

        assertEquals(3L, request.getId());
        assertEquals("Setter comment", request.getText());
    }

    @Test
    void toString_ShouldContainAllFields() {
        CommentCreateRequest request = CommentCreateRequest.builder()
                .id(4L)
                .text("ToString test")
                .build();

        String str = request.toString();

        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("text=ToString test"));
    }

    @Test
    void validation_ShouldFailWhenTextIsBlank() {
        CommentCreateRequest request = CommentCreateRequest.builder()
                .id(1L)
                .text(" ")  // Пробел не считается за заполненный текст
                .build();

        assertTrue(request.getText().isBlank());
    }

    @Test
    void validation_ShouldPassWhenTextIsNotEmpty() {
        CommentCreateRequest request = CommentCreateRequest.builder()
                .id(1L)
                .text("Valid comment")
                .build();

        assertFalse(request.getText().isEmpty());
    }
}