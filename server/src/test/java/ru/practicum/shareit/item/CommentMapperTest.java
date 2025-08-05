package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapperImpl commentMapper;

    private final Long itemId = 1L;
    private final Long userId = 2L;
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    void toComment_ShouldMapCorrectly() {
        CommentCreateRequest request = CommentCreateRequest.builder().text("Великолепно").build();

        Comment comment = commentMapper.toComment(request, itemId, userId);

        assertNotNull(comment);
        assertEquals("Великолепно", comment.getText());
        assertEquals(itemId, comment.getItem().getId());
        assertEquals(userId, comment.getAuthor().getId());
        assertNotNull(comment.getCreated());
    }

    @Test
    void toCommentResponse_ShouldMapCorrectly() {
        User author = User.builder().id(userId).name("Author").email("author@mail.ru").build();

        Item item = Item.builder()
                .id(itemId)
                .name("Item Name")
                .description("Item Description")
                .available(true)
                .owner(User.builder().id(1L).name("Owner").email("Owner@mail.ru").build())
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Comment text")
                .item(item)
                .author(author)
                .created(now)
                .build();

        CommentResponse response = commentMapper.toCommentResponse(comment);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Comment text", response.getText());
        assertEquals("Author", response.getAuthorName());
        assertEquals("Item Name", response.getItemName());
        assertEquals(now, response.getCreated());
    }

    @Test
    void toCommentResponseList_ShouldMapList() {
        Comment comment1 = Comment.builder()
                .id(1L)
                .text("Comment")
                .author(User.builder().id(userId).name("Author").email("author@mail.ru").build())
                .item(Item.builder()
                        .id(itemId)
                        .name("Item Name")
                        .description("Item Description")
                        .available(true)
                        .owner(User.builder().id(1L).name("Owner").email("Owner@mail.ru").build())
                        .build())
                .build();

        List<CommentResponse> responses = commentMapper.toCommentResponseList(List.of(comment1));

        assertNotNull(responses);
        assertEquals(1, responses.size());

        assertEquals("Comment", responses.get(0).getText());
        assertEquals("Author", responses.get(0).getAuthorName());
    }

    @Test
    void toCommentResponse_ShouldHandleNull() {
        assertNull(commentMapper.toCommentResponse(null));
    }

    @Test
    void toCommentResponseList_ShouldHandleNull() {
        assertNull(commentMapper.toCommentResponseList(null));
    }
}