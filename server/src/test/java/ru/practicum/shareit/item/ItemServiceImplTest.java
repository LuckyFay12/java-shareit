package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingShortInfoDto;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    private final Long userId = 1L;
    private final Long itemId = 1L;
    private final Long requestId = 1L;

    private final User owner = User.builder().id(userId).name("Owner").email("owner@mail.ru").build();
    private final Item item = Item.builder()
            .id(itemId)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(owner)
            .build();

    private final ItemRequest itemRequest = ItemRequest.builder().id(requestId).build();

    @Test
    void create_ShouldCreateItemWithoutRequest() {
        when(userService.getById(userId)).thenReturn(owner);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.create(userId, item);

        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        verify(itemRepository).save(item);
    }

    @Test
    void create_ShouldCreateItemWithRequest() {
        item.setItemRequest(itemRequest);
        when(userService.getById(userId)).thenReturn(owner);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.create(userId, item);

        assertNotNull(result);
        assertEquals(itemRequest, result.getItemRequest());
        verify(itemRequestRepository).findById(requestId);
    }

    @Test
    void create_WhenRequestNotFound_ShouldThrow() {
        item.setItemRequest(itemRequest);
        when(userService.getById(userId)).thenReturn(owner);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class,
                () -> itemService.create(userId, item));
    }

    @Test
    void update_ShouldUpdateItemFields() {
        Item updatedItem = Item.builder()
                .name("New Name")
                .description("New Desc")
                .available(false)
                .build();

        when(userService.getById(userId)).thenReturn(owner);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.update(userId, itemId, updatedItem);

        assertEquals("New Name", result.getName());
        assertEquals("New Desc", result.getDescription());
        assertFalse(result.getAvailable());
    }

    @Test
    void update_ShouldThrowWhenNotOwner() {
        User otherUser = User.builder().id(3L).name("otherUser").email("otherUser@mail.ru").build();
        when(userService.getById(otherUser.getId())).thenReturn(otherUser);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(AccessDeniedException.class, () ->
                itemService.update(otherUser.getId(), itemId, new Item()));
    }

    @Test
    void update_WhenItemNotFound_ShouldThrow() {
        when(userService.getById(userId)).thenReturn(owner);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> itemService.update(userId, itemId, new Item()));
    }

    @Test
    void update_WhenNotOwner_ShouldThrow() {
        Long otherUserId = 2L;
        when(userService.getById(otherUserId)).thenReturn(User.builder().id(otherUserId).build());
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(AccessDeniedException.class,
                () -> itemService.update(otherUserId, itemId, new Item()));
    }

    @Test
    void getById_ShouldReturnWithBookingsForOwner() {
        Booking nextBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .end(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .item(item)
                .booker(User.builder().id(2L).name("Kate").email("kate@mail.ru").build())
                .status(Status.WAITING)
                .build();

        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .end(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .item(item)
                .booker(User.builder().id(2L).name("Ann").email("ann@mail.ru").build())
                .status(Status.APPROVED)
                .build();

        BookingShortInfoDto nextBookingDto = BookingShortInfoDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .end(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .build();

        BookingShortInfoDto lastBookingDto = BookingShortInfoDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .end(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .build();

        ItemResponse itemResponse = ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerName(owner.getName())
                .requestId(1L)
                .comments(new ArrayList<>())
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemMapper.toItemResponse(item)).thenReturn(itemResponse);
        when(bookingRepository.findFirstByItemAndStartAfterAndStatusInOrderByStartAsc(
                any(), any(), anyList())).thenReturn(Optional.of(nextBooking));
        when(bookingRepository.findFirstByItemAndEndBeforeAndStatusInOrderByEndDesc(
                any(), any(), anyList())).thenReturn(Optional.of(lastBooking));
        when(bookingMapper.toShortInfoDto(nextBooking)).thenReturn(nextBookingDto);
        when(bookingMapper.toShortInfoDto(lastBooking)).thenReturn(lastBookingDto);

        ItemResponse result = itemService.getById(userId, itemId);

        assertNotNull(result);
        assertEquals(lastBookingDto, result.getLastBooking());
    }

    @Test
    void getById_ShouldReturnItemWithoutBookingsForNonOwner() {
        Long otherUserId = 2L;
        ItemResponse response = new ItemResponse();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemMapper.toItemResponse(item)).thenReturn(response);

        ItemResponse result = itemService.getById(otherUserId, itemId);

        assertEquals(response, result);
        assertNull(result.getNextBooking());
        assertNull(result.getLastBooking());
    }

    @Test
    void getUserItems_ShouldReturnUserItems() {
        List<Item> expectedItems = List.of(item);
        when(userService.getById(userId)).thenReturn(owner);
        when(itemRepository.findAllByOwnerOrderById(owner)).thenReturn(expectedItems);

        List<Item> result = itemService.getUserItems(userId);

        assertEquals(expectedItems, result);
    }

    @Test
    void search_ShouldReturnEmptyForBlankText() {
        List<Item> result = itemService.search("   ");
        assertTrue(result.isEmpty());
    }

    @Test
    void search_ShouldReturnItems() {
        when(itemRepository.search("test")).thenReturn(List.of(item));
        List<Item> result = itemService.search("test");
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }

    @Test
    void addComment_ShouldAddComment() {
        LocalDateTime now = LocalDateTime.now();
        User author = User.builder().id(2L).name("Ann").email("ann@mail.ru").build();
        Comment comment = Comment.builder()
                .id(1L)
                .text("New Comment")
                .item(item)
                .author(author)
                .created(now)
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(author)
                .status(Status.APPROVED)
                .build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.getById(author.getId())).thenReturn(author);
        when(bookingRepository.findAllByItemAndBooker(item, author))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = itemService.addComment(comment);

        assertNotNull(result);
        assertNotNull(result.getCreated());
        verify(commentRepository).save(comment);
    }

    @Test
    void addComment_ShouldThrowWhenNoBookings() {
        Comment comment = Comment.builder()
                .item(item)
                .author(User.builder().id(2L).build())
                .build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.getById(2L)).thenReturn(comment.getAuthor());
        when(bookingRepository.findAllByItemAndBooker(item, comment.getAuthor()))
                .thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> itemService.addComment(comment));
    }
}
