package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}

