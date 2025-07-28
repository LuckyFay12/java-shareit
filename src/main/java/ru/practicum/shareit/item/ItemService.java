package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Long userId, Item item);

    Item update(Long userId, Long itemId, Item item);

    ItemResponse getById(Long userId, Long itemId);

    List<Item> getUserItems(Long userId);

    List<Item> search(String text);

    Comment addComment(Comment comment);
}
