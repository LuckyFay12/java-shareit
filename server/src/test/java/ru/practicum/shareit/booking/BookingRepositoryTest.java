package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingRepositoryTest {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        owner = userRepository.save(User.builder().name("Owner").email("owner@mail.ru").build());
        booker = userRepository.save(User.builder().name("Booker").email("booker@mail.ru").build());
        item = itemRepository.save(Item.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .owner(owner)
                .build());
    }

    @Test
    void findAllByItemOwnerOrderByStartDesc_ShouldReturnOwnerBookings() {
        Booking past = createBooking(now.minusDays(2), now.minusDays(1), Status.APPROVED);
        Booking current = createBooking(now.minusHours(1), now.plusHours(1), Status.APPROVED);
        Booking future = createBooking(now.plusDays(1), now.plusDays(2), Status.WAITING);

        List<Booking> result = bookingRepository.findAllByItemOwnerOrderByStartDesc(owner);

        assertEquals(3, result.size());
        assertEquals(future.getId(), result.get(0).getId());
        assertEquals(current.getId(), result.get(1).getId());
        assertEquals(past.getId(), result.get(2).getId());
    }

    @Test
    void findCurrentByOwnerItems_ShouldReturnCurrentBookings() {
        createBooking(now.minusDays(2), now.minusDays(1), Status.APPROVED);
        Booking current = createBooking(now.minusHours(1), now.plusHours(1), Status.APPROVED);
        createBooking(now.plusDays(1), now.plusDays(2), Status.WAITING);

        List<Booking> result = bookingRepository.findCurrentByOwnerItems(owner, now);

        assertEquals(1, result.size());
        assertEquals(current.getId(), result.get(0).getId());
    }

    @Test
    void findPastByOwnerItems_ShouldReturnPastBookings() {
        Booking past = createBooking(now.minusDays(2), now.minusDays(1), Status.APPROVED);
        createBooking(now.minusHours(1), now.plusHours(1), Status.APPROVED); // current
        createBooking(now.plusDays(1), now.plusDays(2), Status.WAITING); // future

        List<Booking> result = bookingRepository.findPastByOwnerItems(owner, now);

        assertEquals(1, result.size());
        assertEquals(past.getId(), result.get(0).getId());
    }

    @Test
    void findAllByOwnerItemsAndStatusList_ShouldFilterByStatus() {
        createBooking(now.plusDays(1), now.plusDays(2), Status.WAITING);
        Booking approved = createBooking(now.plusDays(3), now.plusDays(4), Status.APPROVED);
        Booking rejected = createBooking(now.plusDays(5), now.plusDays(6), Status.REJECTED);

        List<Booking> result = bookingRepository.findAllByOwnerItemsAndStatusList(
                owner, List.of(Status.APPROVED, Status.REJECTED));

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(b -> b.getId().equals(approved.getId())));
        assertTrue(result.stream().anyMatch(b -> b.getId().equals(rejected.getId())));
    }

    @Test
    void findFirstByItemAndStartAfterAndStatusInOrderByStartAsc_ShouldReturnNextBooking() {
        createBooking(now.minusDays(2), now.minusDays(1), Status.APPROVED);
        Booking next = createBooking(now.plusDays(1), now.plusDays(2), Status.APPROVED);
        createBooking(now.plusDays(3), now.plusDays(4), Status.APPROVED);

        Optional<Booking> result = bookingRepository.findFirstByItemAndStartAfterAndStatusInOrderByStartAsc(item, now,
                List.of(Status.APPROVED));

        assertTrue(result.isPresent());
        assertEquals(next.getId(), result.get().getId());
    }

    private Booking createBooking(LocalDateTime start, LocalDateTime end, Status status) {
        return bookingRepository.save(Booking.builder()
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(status)
                .build());
    }
}
