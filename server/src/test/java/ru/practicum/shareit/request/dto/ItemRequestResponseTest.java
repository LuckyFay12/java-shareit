package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestResponseTest {

    private final LocalDateTime testTime = LocalDateTime.of(2023, 6, 15, 14, 30, 0);
    private final ItemResponse testItem = ItemResponse.builder()
            .id(1L)
            .name("Test Item")
            .description("Test Description")
            .available(true)
            .build();

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        ItemRequestResponse response = ItemRequestResponse.builder()
                .id(1L)
                .description("Need a power drill")
                .requestorId(10L)
                .created(testTime)
                .items(List.of(testItem))
                .build();

        assertEquals(1L, response.getId());
        assertEquals("Need a power drill", response.getDescription());
        assertEquals(10L, response.getRequestorId());
        assertEquals(testTime, response.getCreated());
        assertEquals(1, response.getItems().size());
        assertEquals(testItem, response.getItems().get(0));
    }

    @Test
    void shouldHandleEmptyItemList() {
        ItemRequestResponse response = ItemRequestResponse.builder()
                .id(2L)
                .description("Empty items test")
                .items(List.of())
                .build();

        assertTrue(response.getItems().isEmpty());
    }

    @Test
    void shouldNotBeEqual_WhenFieldsDiffer() {
        ItemRequestResponse response1 = ItemRequestResponse.builder()
                .id(1L)
                .description("First")
                .build();

        ItemRequestResponse response2 = ItemRequestResponse.builder()
                .id(2L)
                .description("Second")
                .build();

        assertNotEquals(response1, response2);
    }

    @Test
    void toString_ShouldContainAllFields() {
        ItemRequestResponse response = ItemRequestResponse.builder()
                .id(3L)
                .description("toString test")
                .requestorId(3L)
                .created(testTime)
                .items(List.of(testItem))
                .build();

        String str = response.toString();
        assertTrue(str.contains("id=3"));
        assertTrue(str.contains("description=toString test"));
        assertTrue(str.contains("requestorId=3"));
        assertTrue(str.contains("created=" + testTime));
        assertTrue(str.contains("items="));
    }
}