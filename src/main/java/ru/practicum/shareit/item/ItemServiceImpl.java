package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Item create(Long userId, Item item) {
        userService.getById(userId);
        return itemRepository.create(item);
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        userService.getById(userId);
        return itemRepository.update(itemId, item);
    }

    @Override
    public Item getById(Long itemId) {
        Item item = itemRepository.getById(itemId);
        if (Objects.isNull(item)) {
            throw new ItemNotFoundException("Вещь не найдена");
        }
        return item;
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        userService.getById(userId);
        return itemRepository.getUserItems(userId);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty() || text.isBlank()) {
            log.info("Пустой поисковый запрос - возвращаем empty list");
            return Collections.emptyList();
        }
        return itemRepository.search(text.toLowerCase().trim());
    }
}
