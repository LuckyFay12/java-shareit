package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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
class BookingServiceTest {

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
    void getByIdBookingTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Booking result = bookingService.getById(booking.getId(), booker.getId());

        assertEquals(booking, result);
    }

    @Test
    void getById_ShouldThrowForUnauthorized() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.getById(booking.getId(), 999L));
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
}
