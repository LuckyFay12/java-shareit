package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ItemRequestMapperImpl itemRequestMapper;

    private final Long userId = 1L;
    private final Long requestId = 1L;
    private final User requestor = User.builder().id(userId).name("User").email("user@mail.ru").build();
    private final LocalDateTime created = LocalDateTime.now();

    private final ItemRequestDto requestDto = ItemRequestDto.builder().description("Need item").build();

    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(requestId)
            .description("Need item")
            .requestor(requestor)
            .created(created)
            .build();

    @Test
    void toItemRequest_ShouldMapCorrectly() {
        ItemRequest result = itemRequestMapper.toItemRequest(userId, requestDto);

        assertNotNull(result);
        assertEquals("Need item", result.getDescription());
        assertEquals(userId, result.getRequestor().getId());
        assertNull(result.getCreated());
    }

    @Test
    void toItemRequestResponse_ShouldMapBasicFields() {
        ItemRequestResponse response = itemRequestMapper.toItemRequestResponse(itemRequest);

        assertNotNull(response);
        assertEquals(requestId, response.getId());
        assertEquals("Need item", response.getDescription());
        assertEquals(userId, response.getRequestorId());
        assertEquals(created, response.getCreated());
        assertNull(response.getItems()); // Поле должно игнорироваться
    }

    @Test
    void toItemRequestResponse_ShouldHandleNull() {
        assertNull(itemRequestMapper.toItemRequestResponse(null));
    }
}