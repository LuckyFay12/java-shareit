package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final User owner = User.builder().id(1L).name("Owner").email("owner@mail.ru").build();
    private final User booker = User.builder().id(2L).name("Booker").email("booker@mail.ru").build();

    private final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .available(true)
            .owner(owner)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2))
            .item(item)
            .booker(booker)
            .status(Status.WAITING)
            .build();

    @Test
    void createBookingTest() {
        when(userService.getById(booker.getId())).thenReturn(booker);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.create(booker.getId(), booking);

        assertNotNull(result);
        assertEquals(Status.WAITING, result.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void create_ShouldThrowWhenItemNotAvailable() {
        item.setAvailable(false);
        when(userService.getById(booker.getId())).thenReturn(booker);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                () -> bookingService.create(booker.getId(), booking));
    }

    @Test
    void createWhenItemOwnerEqualsBookerShouldThrowValidationException() {
        Booking booking = Booking.builder()
                .item(item)
                .booker(owner)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        when(userService.getById(owner.getId())).thenReturn(owner);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                () -> bookingService.create(owner.getId(), booking));
    }

    @Test
    void createNonExistingItem_ShouldThrowItemNotFoundException() {
        Long nonExistingItemId = 999L;

        Booking booking = Booking.builder()
                .item(Item.builder().id(nonExistingItemId).build())
                .booker(booker)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(itemRepository.findById(nonExistingItemId)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> bookingService.create(booker.getId(), booking),
                "Должно выбрасываться исключение при попытке бронирования несуществующей вещи");
    }

    @Test
    void approveBookingTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking saved = invocation.getArgument(0);

            assertEquals(Status.APPROVED, saved.getStatus());
            return saved;
        });

        Booking result = bookingService.approve(booking.getId(), owner.getId(), true);

        assertNotNull(result);
        assertEquals(Status.APPROVED, booking.getStatus());
        assertEquals(Status.APPROVED, result.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void approve_ShouldThrowWhenNotOwner() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.approve(booking.getId(), 999L, true));
    }

    @Test
    void approve_ShouldSetStatusRejected_WhenApprovedFalse() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking saved = invocation.getArgument(0);
            assertEquals(Status.REJECTED, saved.getStatus());  // Проверяем, что статус REJECTED
            return saved;
        });

        Booking result = bookingService.approve(booking.getId(), owner.getId(), false);

        assertEquals(Status.REJECTED, result.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void approve_whenStatusNotWaiting_shouldThrowValidationException() {
        Long userId = 1L;
        Long bookingId = 1L;

        User owner = new User();
        owner.setId(userId);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setStatus(Status.APPROVED);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> bookingService.approve(bookingId, userId, true));
    }

    @Test
    void getByIdBookingTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Booking result = bookingService.getById(booking.getId(), booker.getId());

        assertEquals(booking, result);
    }

    @Test
    void getById_ShouldThrowItemNotFoundException_WhenBookingNotFound() {
        Long nonExistingBookingId = 999L;
        when(bookingRepository.findById(nonExistingBookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.getById(nonExistingBookingId, booker.getId()),
                "Должно выбрасываться исключение, если бронирование не найдено");
    }

    @Test
    void getById_ShouldThrowForUnauthorized() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.getById(booking.getId(), 999L));
    }

    @Test
    void getById_whenUserNotBookerOrOwner_shouldThrowAccessDeniedException() {
        Long userId = 1L;
        Long bookingId = 1L;

        User booker = new User();
        booker.setId(2L);

        User owner = new User();
        owner.setId(3L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class, () -> bookingService.getById(bookingId, userId));
    }

    @Test
    void getAllBookingsTest() {
        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerOrderByStartDesc(booker))
                .thenReturn(List.of(booking));

        List<Booking> result = bookingService.getAll(booker.getId(), State.ALL);

        assertEquals(1, result.size());
        assertEquals(booking, result.get(0));
    }

    @Test
    void getAllBookingsForOwner_ShouldReturnAllForOwner() {
        when(userService.getById(owner.getId())).thenReturn(owner);
        when(itemRepository.findAllByOwnerOrderById(owner)).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemOwnerOrderByStartDesc(owner))
                .thenReturn(List.of(booking));

        List<Booking> result = bookingService.getAllBookingsForOwner(owner.getId(), State.ALL);

        assertEquals(1, result.size());
        assertEquals(booking, result.get(0));
    }

    @Test
    void getAll_WhenStateWaiting_ShouldReturnWaitingBookings() {
        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerAndStatusList(booker, List.of(Status.WAITING)))
                .thenReturn(List.of(booking));

        List<Booking> result = bookingService.getAll(booker.getId(), State.WAITING);

        assertEquals(1, result.size());
        assertEquals(Status.WAITING, result.get(0).getStatus());
    }

    @Test
    void getAll_WhenStateRejected_ShouldReturnRejectedBookings() {
        Booking rejectedBooking = Booking.builder().status(Status.REJECTED).build();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findAllByBookerAndStatusList(booker,
                List.of(Status.REJECTED, Status.CANCELED)))
                .thenReturn(List.of(rejectedBooking));

        List<Booking> result = bookingService.getAll(booker.getId(), State.REJECTED);

        assertEquals(1, result.size());
        assertEquals(Status.REJECTED, result.get(0).getStatus());
    }

    @Test
    void getAll_WhenStateCurrent_ShouldReturnCurrentBookings() {
        Booking currentBooking = Booking.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(1))
                .item(item)
                .booker(booker)
                .build();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findCurrentByBookerItems(eq(booker), any(LocalDateTime.class)))
                .thenReturn(List.of(currentBooking));

        List<Booking> result = bookingService.getAll(booker.getId(), State.CURRENT);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(result.get(0).getEnd().isAfter(LocalDateTime.now()));
    }

    @Test
    void getAll_WhenStateFuture_ShouldReturnFutureBookings() {
        LocalDateTime testStartTime = LocalDateTime.now();

        Booking futureBooking = Booking.builder()
                .start(testStartTime.plusHours(1))
                .end(testStartTime.plusHours(2))
                .item(item)
                .booker(booker)
                .build();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findFutureByBookerItems(eq(booker), any(LocalDateTime.class)))
                .thenReturn(List.of(futureBooking));

        List<Booking> result = bookingService.getAll(booker.getId(), State.FUTURE);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getStart().isAfter(testStartTime));
    }

    @Test
    void getAll_WhenStatePast_ShouldReturnPastBookings() {
        Booking pastBooking = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .build();

        when(userService.getById(booker.getId())).thenReturn(booker);
        when(bookingRepository.findPastByBookerItems(eq(booker), any(LocalDateTime.class)))
                .thenReturn(List.of(pastBooking));

        List<Booking> result = bookingService.getAll(booker.getId(), State.PAST);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getEnd().isBefore(LocalDateTime.now()));
    }

    @Test
    void getAllBookingsForOwner_WhenNoItems_ShouldThrowValidationException() {
        when(userService.getById(owner.getId())).thenReturn(owner);
        when(itemRepository.findAllByOwnerOrderById(owner)).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class,
                () -> bookingService.getAllBookingsForOwner(owner.getId(), State.ALL),
                "Список бронирований могут получать пользователи, которые владеют хотя бы 1 вещью");
    }

    @Test
    void getAllBookingsForOwner_WhenStateWaiting_ShouldReturnWaitingBookings() {
        when(userService.getById(owner.getId())).thenReturn(owner);
        when(itemRepository.findAllByOwnerOrderById(owner)).thenReturn(List.of(item));
        when(bookingRepository.findAllByOwnerItemsAndStatusList(owner, List.of(Status.WAITING)))
                .thenReturn(List.of(booking));

        List<Booking> result = bookingService.getAllBookingsForOwner(owner.getId(), State.WAITING);

        assertEquals(1, result.size());
        assertEquals(Status.WAITING, result.get(0).getStatus());
    }

    @Test
    void getAllBookingsForOwner_WhenStateCurrent_ShouldReturnCurrentBookings() {
        Booking currentBooking = Booking.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(1))
                .item(item)
                .booker(booker)
                .build();

        when(userService.getById(owner.getId())).thenReturn(owner);
        when(itemRepository.findAllByOwnerOrderById(owner)).thenReturn(List.of(item));
        when(bookingRepository.findCurrentByOwnerItems(eq(owner), any(LocalDateTime.class)))
                .thenReturn(List.of(currentBooking));

        List<Booking> result = bookingService.getAllBookingsForOwner(owner.getId(), State.CURRENT);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(result.get(0).getEnd().isAfter(LocalDateTime.now()));
    }
}

