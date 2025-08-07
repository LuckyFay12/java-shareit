package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void builder_ShouldCreateObjectWithSpecifiedValues() {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .name("Ann")
                .email("ann@example.com")
                .build();

        assertEquals(1L, response.getId());
        assertEquals("Ann", response.getName());
        assertEquals("ann@example.com", response.getEmail());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        UserResponse response = new UserResponse(2L, "Alice", "alice@example.com");

        assertEquals(2L, response.getId());
        assertEquals("Alice", response.getName());
        assertEquals("alice@example.com", response.getEmail());
    }

    @Test
    void dataAnnotation_ShouldProvideGettersAndSetters() {
        UserResponse response = new UserResponse();
        response.setId(3L);
        response.setName("Ivan");
        response.setEmail("ivan@example.com");

        assertEquals(3L, response.getId());
        assertEquals("Ivan", response.getName());
        assertEquals("ivan@example.com", response.getEmail());
    }

    @Test
    void shouldNotBeEqual_WhenFieldsDiffer() {
        UserResponse response1 = UserResponse.builder()
                .id(1L)
                .name("User 1")
                .build();

        UserResponse response2 = UserResponse.builder()
                .id(2L)
                .name("User 2")
                .build();

        assertNotEquals(response1, response2);
    }

    @Test
    void toString_ShouldContainAllFields() {
        UserResponse response = UserResponse.builder()
                .id(4L)
                .name("Test User")
                .email("test@example.com")
                .build();

        String str = response.toString();

        assertTrue(str.contains("id=4"));
        assertTrue(str.contains("name=Test User"));
        assertTrue(str.contains("email=test@example.com"));
    }

    @Test
    void jsonSerialization_ShouldWorkCorrectly() throws JsonProcessingException {
        UserResponse response = UserResponse.builder()
                .id(5L)
                .name("JSON Test")
                .email("json@test.com")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);

        assertTrue(json.contains("\"id\":5"));
        assertTrue(json.contains("\"name\":\"JSON Test\""));
        assertTrue(json.contains("\"email\":\"json@test.com\""));
    }

    @Test
    void shouldHandleNullValues() {
        UserResponse response = UserResponse.builder().build();

        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getEmail());
        assertNotNull(response.toString());
    }
}