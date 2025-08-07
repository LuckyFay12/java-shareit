package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCreateRequestTest {

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        ItemCreateRequest request = new ItemCreateRequest();

        assertNull(request.getId());
        assertNull(request.getName());
        assertNull(request.getDescription());
        assertNull(request.getAvailable());
        assertNull(request.getRequestId());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        ItemCreateRequest request = new ItemCreateRequest(
                1L,
                "Drill",
                "Powerful drill with 20V battery",
                true,
                10L
        );

        assertEquals(1L, request.getId());
        assertEquals("Drill", request.getName());
        assertEquals("Powerful drill with 20V battery", request.getDescription());
        assertTrue(request.getAvailable());
        assertEquals(10L, request.getRequestId());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        ItemCreateRequest request = ItemCreateRequest.builder()
                .id(2L)
                .name("Hammer")
                .description("Steel hammer with rubber handle")
                .available(false)
                .requestId(20L)
                .build();

        assertEquals(2L, request.getId());
        assertEquals("Hammer", request.getName());
        assertEquals("Steel hammer with rubber handle", request.getDescription());
        assertFalse(request.getAvailable());
        assertEquals(20L, request.getRequestId());
    }

    @Test
    void setters_ShouldSetValuesCorrectly() {
        ItemCreateRequest request = new ItemCreateRequest();

        request.setId(3L);
        request.setName("Screwdriver");
        request.setDescription("Set of 5 screwdrivers");
        request.setAvailable(true);
        request.setRequestId(30L);

        assertEquals(3L, request.getId());
        assertEquals("Screwdriver", request.getName());
        assertEquals("Set of 5 screwdrivers", request.getDescription());
        assertTrue(request.getAvailable());
        assertEquals(30L, request.getRequestId());
    }

    @Test
    void toString_ShouldContainAllFields() {
        ItemCreateRequest request = ItemCreateRequest.builder()
                .id(4L)
                .name("Pliers")
                .description("Professional pliers")
                .available(true)
                .requestId(40L)
                .build();

        String str = request.toString();

        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("name=Pliers"));
        assertTrue(str.contains("description=Professional pliers"));
        assertTrue(str.contains("available=true"));
        assertTrue(str.contains("requestId=40"));
    }
}