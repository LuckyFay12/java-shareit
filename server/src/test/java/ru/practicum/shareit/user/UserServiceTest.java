package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final User user = User.builder().id(1L).name("Ivan").email("ivan@example.com").build();

    @Test
    void createUser_WithUniqueEmail() {
        when(userRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test
    void create_ShouldThrowException_WhenEmailExists() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        assertThrows(AlreadyExistsException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserTest() {
        User updatedUser = User.builder().name("Petr").email("petr@mail.ru").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.update(1L, updatedUser);

        assertEquals("Petr", result.getName());
        assertEquals("petr@mail.ru", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.update(1L, new User()));
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () ->
                userService.delete(1L));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void deleteUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertEquals(user, result);
    }

    @Test
    void getUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getById(1L));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers_WhenUserExists() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAll();

        assertNotNull(result);
        assertEquals(user, result.get(0));
    }

    @Test
    void validateEmailExists_WhenEmailUnique() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertDoesNotThrow(() -> userService.create(user));
    }
}





