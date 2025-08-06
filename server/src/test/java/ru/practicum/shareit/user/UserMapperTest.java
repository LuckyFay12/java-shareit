package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void toUser_ShouldMapCorrectly() {
        UserCreateRequest request = UserCreateRequest.builder().name("Ivan").email("ivan@mail.ru").build();
        User user = userMapper.toUser(request);

        assertEquals(request.getName(), user.getName());
        assertEquals(request.getEmail(), user.getEmail());
        assertNull(user.getId());
    }

    @Test
    void toUser_FromUpdateRequest_ShouldMapCorrectly() {
        UserUpdateRequest request = UserUpdateRequest.builder().name("Ivan").email("ivan@mail.ru").build();

        User user = userMapper.toUser(request);

        assertEquals(request.getName(), user.getName());
        assertEquals(request.getEmail(), user.getEmail());
        assertNull(user.getId());
    }

    @Test
    void toUserResponse_ShouldMapCorrectly() {
        User user = User.builder().id(1L).name("Petr").email("petr@mail.ru").build();
        UserResponse response = userMapper.toUserResponse(user);

        assertEquals(user.getId(), response.getId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
    }
}


