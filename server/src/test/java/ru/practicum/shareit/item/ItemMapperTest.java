package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemMapperImpl itemMapper;

    private final User owner = User.builder()
            .id(1L)
            .name("Owner")
            .email("owner@mail.ru")
            .build();

    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Need item")
            .build();

    @Test
    void toItem_FromCreateRequest_ShouldMapCorrectly() {
        ItemCreateRequest request = ItemCreateRequest.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .requestId(1L)
                .build();
        Long userId = 1L;
        Item result = itemMapper.toItem(request, userId);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getAvailable(), result.getAvailable());
        assertEquals(userId, result.getOwner().getId());
        assertEquals(request.getRequestId(), result.getItemRequest().getId());
    }

    @Test
    void toItem_FromUpdateRequest_ShouldMapCorrectly() {
        ItemUpdateRequest request = ItemUpdateRequest.builder()
                .name("Updated Item")
                .description("Updated Description")
                .available(false)
                .build();
        Long userId = 1L;

        Item result = itemMapper.toItem(request, userId);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getAvailable(), result.getAvailable());
        assertEquals(userId, result.getOwner().getId());
        assertNull(result.getItemRequest());
    }

    @Test
    void toItemResponse_ShouldMapCorrectly() {
        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(owner)
                .itemRequest(itemRequest)
                .build();
        ItemResponse result = itemMapper.toItemResponse(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(owner.getName(), result.getOwnerName());
        assertEquals(itemRequest.getId(), result.getRequestId());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
    }

    @Test
    void toItemResponse_WithNullItem_ShouldReturnNull() {
        assertNull(itemMapper.toItemResponse(null));
    }

    @Test
    void loadComments_whenCommentsExist_shouldReturnMappedComments() {
        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);

        CommentResponse response = new CommentResponse();
        response.setId(1L);

        when(commentRepository.findAllByItem(item))
                .thenReturn(List.of(comment));
        when(commentMapper.toCommentResponse(comment))
                .thenReturn(response);

        List<CommentResponse> result = itemMapper.loadComments(item);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(commentRepository).findAllByItem(item);
        verify(commentMapper).toCommentResponse(comment);
    }
}


