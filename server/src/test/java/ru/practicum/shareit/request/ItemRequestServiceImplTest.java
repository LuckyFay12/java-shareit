package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private final Long userId = 1L;
    private final Long requestId = 1L;
    private final User user = User.builder().id(userId).name("User").email("user@example.ru").build();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(requestId)
            .description("Need item")
            .requestor(user)
            .created(LocalDateTime.now())
            .build();

    @Test
    void create_ShouldSaveRequest() {
        when(userService.getById(userId)).thenReturn(user);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequest result = itemRequestService.create(userId, itemRequest);

        assertNotNull(result);
        assertNotNull(result.getCreated());
        verify(itemRequestRepository).save(itemRequest);
        assertTrue(result.getCreated().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void getUserRequests_ShouldReturnUserRequests() {
        when(userService.getById(userId)).thenReturn(user);
        when(itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId))
                .thenReturn(List.of(itemRequest));

        List<ItemRequest> result = itemRequestService.getUserRequests(userId);

        assertEquals(1, result.size());
        assertEquals(itemRequest, result.get(0));
        verify(userService).getById(userId);
    }

    @Test
    void getAllRequests_ShouldReturnOtherUsersRequests() {
        Long userId2 = 2L;
        User user2 = User.builder().id(userId2).name("User2").email("user2@example.ru").build();

        when(userService.getById(userId2)).thenReturn(user2);
        when(itemRequestRepository.findAllOtherUsersRequests(userId2))
                .thenReturn(List.of(itemRequest));

        List<ItemRequest> result = itemRequestService.getAllRequests(userId2);

        assertEquals(1, result.size());
        assertEquals(itemRequest, result.get(0));
        verify(itemRequestRepository).findAllOtherUsersRequests(userId2);
    }

    @Test
    void getRequestById_ShouldReturnRequestWithItems() {
        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(User.builder().id(2L).name("User2").email("user2@example.ru").build())
                .itemRequest(itemRequest)
                .build();

        ItemResponse itemResponse = ItemResponse.builder()
                .id(1L)
                .name(item.getName())
                .description(item.getDescription())
                .available(true)
                .ownerName(item.getOwner().getName())
                .requestId(itemRequest.getId())
                .lastBooking(null)
                .nextBooking(null)
                .build();

        ItemRequestResponse response = ItemRequestResponse.builder()
                .id(requestId)
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .items(List.of(itemResponse))
                .build();

        when(userService.getById(userId)).thenReturn(user);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRequestMapper.toItemRequestResponse(itemRequest)).thenReturn(response);
        when(itemRepository.findAllByItemRequest(itemRequest)).thenReturn(List.of(item));
        when(itemMapper.toItemResponse(item)).thenReturn(itemResponse);

        ItemRequestResponse result = itemRequestService.getRequestById(userId, requestId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals("Item", result.getItems().get(0).getName());
    }

    @Test
    void getRequestById_ShouldThrowWhenNotFound() {
        when(userService.getById(userId)).thenReturn(user);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class, () ->
                itemRequestService.getRequestById(userId, requestId));
    }

    @Test
    void create_ShouldThrowWhenUserNotFound() {
        when(userService.getById(userId)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () ->
                itemRequestService.create(userId, itemRequest));
    }
}