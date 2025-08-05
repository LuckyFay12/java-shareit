package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.user.dto.UserResponse;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BookingResponseTest {

    private final LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
    private final LocalDateTime end = LocalDateTime.of(2025, 1, 2, 10, 0);
    private final UserResponse booker = UserResponse.builder().id(1L).name("Booker").build();
    private final ItemResponse item = ItemResponse.builder().id(1L).name("Item").build();

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        BookingResponse response = new BookingResponse();

        assertNull(response.getId());
        assertNull(response.getStart());
        assertNull(response.getEnd());
        assertNull(response.getBooker());
        assertNull(response.getItem());
        assertNull(response.getStatus());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        BookingResponse response = new BookingResponse(1L, start, end, booker, item, Status.APPROVED);

        assertEquals(1L, response.getId());
        assertEquals(start, response.getStart());
        assertEquals(end, response.getEnd());
        assertEquals(booker, response.getBooker());
        assertEquals(item, response.getItem());
        assertEquals(Status.APPROVED, response.getStatus());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        BookingResponse response = BookingResponse.builder()
                .id(2L)
                .start(start)
                .end(end)
                .booker(booker)
                .item(item)
                .status(Status.WAITING)
                .build();

        assertEquals(2L, response.getId());
        assertEquals(start, response.getStart());
        assertEquals(end, response.getEnd());
        assertEquals(booker, response.getBooker());
        assertEquals(item, response.getItem());
        assertEquals(Status.WAITING, response.getStatus());
    }

    @Test
    void setters_ShouldSetValuesCorrectly() {
        BookingResponse response = new BookingResponse();

        response.setId(3L);
        response.setStart(start);
        response.setEnd(end);
        response.setBooker(booker);
        response.setItem(item);
        response.setStatus(Status.REJECTED);

        assertEquals(3L, response.getId());
        assertEquals(start, response.getStart());
        assertEquals(end, response.getEnd());
        assertEquals(booker, response.getBooker());
        assertEquals(item, response.getItem());
        assertEquals(Status.REJECTED, response.getStatus());
    }

    @Test
    void toString_ShouldContainAllFields() {
        BookingResponse response = BookingResponse.builder()
                .id(4L)
                .start(start)
                .end(end)
                .booker(booker)
                .item(item)
                .status(Status.CANCELED)
                .build();

        String str = response.toString();

        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("start=" + start));
        assertTrue(str.contains("end=" + end));
        assertTrue(str.contains("booker=" + booker));
        assertTrue(str.contains("item=" + item));
        assertTrue(str.contains("status=CANCELED"));
    }
}
