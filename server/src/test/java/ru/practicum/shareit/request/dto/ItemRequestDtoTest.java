package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDtoTest {

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        ItemRequestDto dto = new ItemRequestDto();

        assertNull(dto.getId());
        assertNull(dto.getDescription());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        ItemRequestDto dto = new ItemRequestDto(1L, "Description");

        assertEquals(1L, dto.getId());
        assertEquals("Description", dto.getDescription());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(2L)
                .description("Description")
                .build();

        assertEquals(2L, dto.getId());
        assertEquals("Description", dto.getDescription());
    }

    @Test
    void setters_ShouldSetValuesCorrectly() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setId(3L);
        dto.setDescription("Description");

        assertEquals(3L, dto.getId());
        assertEquals("Description", dto.getDescription());
    }

    @Test
    void shouldNotBeEqual_WhenFieldsDiffer() {
        ItemRequestDto dto1 = ItemRequestDto.builder()
                .id(1L)
                .description("First request")
                .build();

        ItemRequestDto dto2 = ItemRequestDto.builder()
                .id(2L)
                .description("Second request")
                .build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void toString_ShouldContainAllFields() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(4L)
                .description("Test toString()")
                .build();

        String str = dto.toString();

        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("description=Test toString()"));
    }

    @Test
    void shouldHandleNullValues() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(null);
        dto.setDescription(null);

        assertNull(dto.getId());
        assertNull(dto.getDescription());
        assertNotNull(dto.toString());
    }
}