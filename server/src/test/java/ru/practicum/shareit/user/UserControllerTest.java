package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerTest {

    @MockBean
    UserService userService;
    @MockBean
    UserMapper userMapper;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void testCreateUser() {
        String name = "Ivanov Ivan";
        String email = "ivanov@mail.ru";
        Long id = 1L;
        UserCreateRequest request = UserCreateRequest.builder().name(name).email(email).build();
        User user = User.builder().id(id).name(name).email(email).build();
        UserResponse response = UserResponse.builder().id(id).name(name).email(email).build();

        when(userMapper.toUser(any(UserCreateRequest.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @SneakyThrows
    void testUpdateUser() {
        String name = "Petrov Petr";
        String email = "petrov@mail.ru";
        Long id = 1L;
        UserUpdateRequest request = UserUpdateRequest.builder().name(name).email(email).build();
        User user = User.builder().id(id).name(name).email(email).build();
        UserResponse response = UserResponse.builder().id(id).name(name).email(email).build();

        when(userMapper.toUser(any(UserUpdateRequest.class))).thenReturn(user);
        when(userService.update(eq(id), any())).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(patch("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @SneakyThrows
    void testDeleteUser() {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testGetById() {
        Long id = 1L;
        String name = "Petrov Petr";
        String email = "petrov@mail.ru";
        User user = User.builder().id(id).name(name).email(email).build();
        UserResponse response = UserResponse.builder().id(id).name(name).email(email).build();

        when(userService.getById(id)).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

        mockMvc.perform(get("/users/{userId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @SneakyThrows
    void testGetAllUsers() {
        User user1 = User.builder().id(1L).name("Kate").email("kate@mail.ru").build();
        User user2 = User.builder().id(2L).name("Ann").email("ann@mail.ru").build();

        UserResponse response1 = UserResponse.builder().id(1L).name("Kate").email("kate@mail.ru").build();
        UserResponse response2 = UserResponse.builder().id(2L).name("Ann").email("ann@mail.ru").build();

        when(userService.getAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toUserResponse(user1)).thenReturn(response1);
        when(userMapper.toUserResponse(user2)).thenReturn(response2);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Kate"))
                .andExpect(jsonPath("$[0].email").value("kate@mail.ru"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Ann"))
                .andExpect(jsonPath("$[1].email").value("ann@mail.ru"));
    }
}

