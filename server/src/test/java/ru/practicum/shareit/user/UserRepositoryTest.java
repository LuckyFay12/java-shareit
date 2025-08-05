package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {

    private final UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@mail.ru")
                .build();
    }

    @Test
    void saveUser_shouldReturnSaveUser() {
        User savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser.getId());

        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertThat(foundUser)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void updateUser_shouldUpdateFields() {
        User savedUser = userRepository.save(user);

        savedUser.setName("Updated Name");
        savedUser.setEmail("updated@mail.ru");
        User updatedUser = userRepository.save(savedUser);

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@mail.ru");
    }

    @Test
    void deleteUser_shouldRemoveFromDatabase() {
        User savedUser = userRepository.save(user);

        userRepository.delete(savedUser);

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }
}


