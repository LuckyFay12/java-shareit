package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {
    private final Item testItem = Item.builder().id(1L).name("Дрель").build();
    private final User testUser = User.builder().id(1L).name("User").build();

    @Test
    void testBookingCreation() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = Booking.builder()
                .start(now)
                .end(now.plusDays(1))
                .item(testItem)
                .booker(testUser)
                .status(Status.WAITING)
                .build();

        assertNull(booking.getId());
        assertEquals(now, booking.getStart());
        assertEquals(now.plusDays(1), booking.getEnd());
        assertEquals(testItem, booking.getItem());
        assertEquals(testUser, booking.getBooker());
        assertEquals(Status.WAITING, booking.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.WAITING)
                .build();

        Booking booking2 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Status.APPROVED)
                .build();

        assertEquals(booking1, booking2);
        assertEquals(booking1.hashCode(), booking2.hashCode());

        Booking booking3 = Booking.builder().id(2L).build();
        assertNotEquals(booking1, booking3);
    }

    @Test
    void testIsFinished() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        Booking finishedBooking = Booking.builder()
                .end(past)
                .status(Status.APPROVED)
                .build();

        assertTrue(finishedBooking.isFinished(LocalDateTime.now()));

        Booking notFinished = Booking.builder()
                .end(LocalDateTime.now().plusDays(1))
                .status(Status.APPROVED)
                .build();

        assertFalse(notFinished.isFinished(LocalDateTime.now()));

        Booking rejected = Booking.builder()
                .end(past)
                .status(Status.REJECTED)
                .build();

        assertFalse(rejected.isFinished(LocalDateTime.now()));
    }
}

