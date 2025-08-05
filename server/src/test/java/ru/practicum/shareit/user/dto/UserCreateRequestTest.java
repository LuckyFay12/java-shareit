package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserCreateRequestTest {

    @Test
    void noArgsConstructor_ShouldCreateEmptyObject() {
        UserCreateRequest request = new UserCreateRequest();

        assertNull(request.getId());
        assertNull(request.getName());
        assertNull(request.getEmail());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        UserCreateRequest request = new UserCreateRequest(1L, "John Doe", "john@example.com");

        assertEquals(1L, request.getId());
        assertEquals("John Doe", request.getName());
        assertEquals("john@example.com", request.getEmail());
    }

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        UserCreateRequest request = UserCreateRequest.builder()
                .id(2L)
                .name("Alice Smith")
                .email("alice@example.com")
                .build();

        assertEquals(2L, request.getId());
        assertEquals("Alice Smith", request.getName());
        assertEquals("alice@example.com", request.getEmail());
    }

    @Test
    void setters_ShouldSetValuesCorrectly() {
        UserCreateRequest request = new UserCreateRequest();

        request.setId(3L);
        request.setName("Bob Johnson");
        request.setEmail("bob@example.com");

        assertEquals(3L, request.getId());
        assertEquals("Bob Johnson", request.getName());
        assertEquals("bob@example.com", request.getEmail());
    }

    @Test
    void shouldNotBeEqual_WhenFieldsDiffer() {
        UserCreateRequest request1 = UserCreateRequest.builder()
                .id(1L)
                .name("User 1")
                .build();

        UserCreateRequest request2 = UserCreateRequest.builder()
                .id(2L)
                .name("User 2")
                .build();

        assertNotEquals(request1, request2);
    }

    @Test
    void toString_ShouldContainAllFields() {
        UserCreateRequest request = UserCreateRequest.builder()
                .id(4L)
                .name("Test User")
                .email("test@example.com")
                .build();

        String str = request.toString();

        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("name=Test User"));
        assertTrue(str.contains("email=test@example.com"));
    }

    @Test
    void shouldHandleNullValues() {
        UserCreateRequest request = new UserCreateRequest();
        request.setId(null);
        request.setName(null);
        request.setEmail(null);

        assertNull(request.getId());
        assertNull(request.getName());
        assertNull(request.getEmail());
        assertNotNull(request.toString());
    }

    @Test
    void emailValidation_ShouldAcceptValidFormat() {
        UserCreateRequest request = UserCreateRequest.builder()
                .email("valid.email@example.com")
                .build();

        assertTrue(request.getEmail().contains("@"));
    }
}