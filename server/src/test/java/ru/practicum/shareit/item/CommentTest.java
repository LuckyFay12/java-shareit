package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {
    private final Item testItem = Item.builder().id(1L).name("Дрель").build();
    private final User testAuthor = User.builder().id(1L).name("Автор").build();
    private final LocalDateTime testTime = LocalDateTime.now();

    @Test
    void testCommentCreation() {
        Comment comment = Comment.builder()
                .text("Отличная дрель!")
                .item(testItem)
                .author(testAuthor)
                .created(testTime)
                .build();

        assertNull(comment.getId());
        assertEquals("Отличная дрель!", comment.getText());
        assertEquals(testItem, comment.getItem());
        assertEquals(testAuthor, comment.getAuthor());
        assertEquals(testTime, comment.getCreated());
    }

    @Test
    void testEqualsAndHashCode() {
        Comment comment1 = Comment.builder()
                .id(1L)
                .text("Comment 1")
                .created(testTime)
                .build();

        Comment comment2 = Comment.builder()
                .id(1L)
                .text("Comment 2")
                .created(testTime.plusHours(1))
                .build();

        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());

        Comment comment3 = Comment.builder().id(2L).build();
        assertNotEquals(comment1, comment3);
    }

    @Test
    void testToString() {
        Comment comment = Comment.builder()
                .id(1L)
                .text("Тестовый комментарий")
                .build();

        assertTrue(comment.toString().contains("Тестовый комментарий"));
    }
}
