package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingCreateRequestTest {

    private final LocalDateTime testStart = LocalDateTime.of(2025, 1, 1, 10, 0);
    private final LocalDateTime testEnd = LocalDateTime.of(2025, 1, 2, 10, 0);

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        BookingCreateRequest request = new BookingCreateRequest();
        assertNull(request.getId());
        assertNull(request.getStart());
        assertNull(request.getEnd());
        assertNull(request.getItemId());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        BookingCreateRequest request = new BookingCreateRequest(1L, testStart, testEnd, 10L);
        assertEquals(1L, request.getId());
        assertEquals(testStart, request.getStart());
        assertEquals(testEnd, request.getEnd());
        assertEquals(10L, request.getItemId());
    }

    @Test
    void setters_ShouldSetValuesCorrectly() {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setId(2L);
        request.setStart(testStart);
        request.setEnd(testEnd);
        request.setItemId(20L);
        assertEquals(2L, request.getId());
        assertEquals(testStart, request.getStart());
        assertEquals(testEnd, request.getEnd());
        assertEquals(20L, request.getItemId());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        BookingCreateRequest request = BookingCreateRequest.builder()
                .id(3L)
                .start(testStart)
                .end(testEnd)
                .itemId(30L)
                .build();
        assertEquals(3L, request.getId());
        assertEquals(testStart, request.getStart());
        assertEquals(testEnd, request.getEnd());
        assertEquals(30L, request.getItemId());
    }

    @Test
    void toString_ShouldContainAllFields() {
        BookingCreateRequest request = new BookingCreateRequest(4L, testStart, testEnd, 40L);
        String str = request.toString();
        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("start=" + testStart));
        assertTrue(str.contains("end=" + testEnd));
        assertTrue(str.contains("itemId=40"));
    }
}
