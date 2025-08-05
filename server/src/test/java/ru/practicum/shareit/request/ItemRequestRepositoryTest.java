package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestRepositoryTest {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private User user1;
    private User user2;
    private ItemRequest request1;
    private ItemRequest request2;
    private ItemRequest request3;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder()
                .name("User 1")
                .email("user1@example.ru")
                .build());

        user2 = userRepository.save(User.builder()
                .name("User 2")
                .email("user2@example.ru")
                .build());

        request1 = itemRequestRepository.save(ItemRequest.builder()
                .id(1L)
                .description("Request 1")
                .requestor(user1)
                .created(LocalDateTime.now().minusDays(2))
                .build());

        request2 = itemRequestRepository.save(ItemRequest.builder()
                .id(2L)
                .description("Request 2")
                .requestor(user2)
                .created(LocalDateTime.now())
                .build());

        request3 = itemRequestRepository.save(ItemRequest.builder()
                .description("Request 3")
                .requestor(user2)
                .created(LocalDateTime.now().minusDays(1))
                .build());
    }

    @Test
    void findByRequestor_IdOrderByCreatedDesc_ShouldReturnUserRequestsInCorrectOrder() {
        List<ItemRequest> requests = itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(user2.getId());

        assertEquals(2, requests.size());
        assertEquals("Request 2", requests.get(0).getDescription());
        assertEquals("Request 3", requests.get(1).getDescription());
    }

    @Test
    void findByRequestor_IdOrderByCreatedDesc_ShouldReturnEmptyListForUnknownUser() {
        List<ItemRequest> requests = itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(12L);

        assertTrue(requests.isEmpty());
    }

    @Test
    void findAllOtherUsersRequests_ShouldReturnOnlyOtherUsersRequests() {
        List<ItemRequest> requests = itemRequestRepository.findAllOtherUsersRequests(user1.getId());

        assertEquals(2, requests.size());
        assertEquals("Request 2", requests.get(0).getDescription());
        assertEquals("Request 3", requests.get(1).getDescription());
        assertEquals(user2.getId(), requests.get(0).getRequestor().getId());
    }

    @Test
    void findAllOtherUsersRequests_ShouldReturnAllForUnknownUser() {
        List<ItemRequest> requests = itemRequestRepository.findAllOtherUsersRequests(12L);

        assertEquals(3, requests.size());
    }

    @Test
    void save_ShouldReturnAllFields() {
        ItemRequest newRequest = ItemRequest.builder()
                .description("New Request")
                .requestor(user1)
                .created(LocalDateTime.now())
                .build();

        ItemRequest saved = itemRequestRepository.save(newRequest);

        assertNotNull(saved.getId());
        assertEquals("New Request", saved.getDescription());
        assertEquals(user1.getId(), saved.getRequestor().getId());
        assertNotNull(saved.getCreated());
    }
}