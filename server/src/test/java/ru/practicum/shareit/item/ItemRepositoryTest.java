package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    private User owner;
    private ItemRequest request;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(User.builder()
                .name("Owner")
                .email("owner@mail.ru")
                .build());

        User requestor = userRepository.save(User.builder()
                .name("Requestor")
                .email("requestor@mail.com")
                .build());

        request = itemRequestRepository.save(ItemRequest.builder()
                .description("Need item")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build());

        item = Item.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .owner(owner)
                .itemRequest(request)
                .build();
    }

    @Test
    void findAllByOwnerOrderById_ShouldReturnOwnerItems() {
        Item savedItem = itemRepository.save(item);

        List<Item> result = itemRepository.findAllByOwnerOrderById(owner);

        assertEquals(1, result.size());
        assertEquals(savedItem.getId(), result.get(0).getId());
    }

    @Test
    void findAllByItemRequest_ShouldReturnRequestedItems() {
        Item savedItem = itemRepository.save(item);

        List<Item> result = itemRepository.findAllByItemRequest(request);

        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(item -> savedItem.getItemRequest().equals(request)));
    }

    @Test
    void search_ShouldFindItemsByText() {
        itemRepository.save(item);

        List<Item> resultByName = itemRepository.search("item");
        assertEquals(1, resultByName.size());
        assertEquals("Item", resultByName.get(0).getName());

        List<Item> resultByDesc = itemRepository.search("desc");
        assertEquals(1, resultByDesc.size());
        assertEquals("Description", resultByDesc.get(0).getDescription());

        List<Item> emptyResult = itemRepository.search("ничего");
        assertTrue(emptyResult.isEmpty());
    }
}
