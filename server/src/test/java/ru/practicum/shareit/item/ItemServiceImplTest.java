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
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @InjectMocks
    private ItemServiceImpl itemService;

    private final Long userId = 1L;
    private final Long itemId = 1L;
    private final User owner = User.builder().id(userId).name("Owner").email("owner@mail.ru").build();
    private final Item item = Item.builder()
            .id(itemId)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(owner)
            .build();

    @Test
    void create_ShouldCreateItem() {
        when(userService.getById(userId)).thenReturn(owner);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.create(userId, item);

        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        verify(itemRepository).save(item);
    }

    @Test
    void update_ShouldUpdateItem() {
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
    void getById_ShouldReturnWithBookingsForOwner() {
        LocalDateTime now = LocalDateTime.now();
        Booking nextBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(User.builder().id(2L).name("Kate").email("kate@mail.ru").build())
                .status(Status.WAITING)
                .build();

        Booking lastBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(User.builder().id(2L).name("Ann").email("ann@mail.ru").build())
                .status(Status.WAITING)
                .build();

        BookingShortInfoDto nextBookingDto = BookingShortInfoDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        BookingShortInfoDto lastBookingDto = BookingShortInfoDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
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
        assertEquals(nextBookingDto, result.getNextBooking());
        assertEquals(lastBookingDto, result.getLastBooking());
    }

    @Test
    void search_ShouldReturnEmptyForBlankText() {
        List<Item> result = itemService.search("   ");
        assertTrue(result.isEmpty());
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
}
