package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ItemRepository {
    private Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 1L;

    public Item create(Item item) {
        item.setId(idCounter++);
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Long itemId, Item item) {
        Item stored = items.get(itemId);

        if (Objects.isNull(stored)) {
            return null;
        }
        if (Objects.nonNull(item.getName()) && !Objects.equals(stored.getName(), item.getName())) {
            stored.setName(item.getName());
        }
        if (Objects.nonNull(item.getDescription()) && !Objects.equals(stored.getDescription(), item.getDescription())) {
            stored.setDescription(item.getDescription());
        }
        if (Objects.nonNull(item.getAvailable()) && !Objects.equals(stored.getAvailable(), item.getAvailable())) {
            stored.setAvailable(item.getAvailable());
        }
        return stored;
    }

    public Item getById(Long itemId) {
        return items.get(itemId);
    }

    public List<Item> getUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Item> search(String text) {
        return items.values().stream()
                .filter(item -> item.getAvailable())
                .filter(item -> item.getName().toLowerCase().contains(text) ||
                                item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }
}
