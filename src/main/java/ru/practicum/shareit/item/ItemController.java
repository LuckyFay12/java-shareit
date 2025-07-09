package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemResponse create(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @Valid @RequestBody ItemCreateRequest request) {
        log.info("Получен HTTP-запрос на добавление вещи: {}", request);
        Item createdItem = itemService.create(userId, itemMapper.toItem(request, userId));
        return itemMapper.toItemResponse(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse update(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @Min(1L) @PathVariable Long itemId,
                               @Valid @RequestBody ItemUpdateRequest request) {
        log.info("Получен HTTP-запрос на обновление вещи с id {}", itemId);
        Item updatedItem = itemService.update(userId, itemId, itemMapper.toItem(request, userId));
        return itemMapper.toItemResponse(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemResponse get(@Min(1L) @PathVariable Long itemId) {
        log.info("Получен HTTP-запрос на получение вещи с id {}", itemId);
        return itemMapper.toItemResponse(itemService.getById(itemId));
    }

    @GetMapping
    public List<ItemResponse> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP-запрос на получение всех вещей пользователя id {}", userId);
        return itemService.getUserItems(userId).stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemResponse> search(@RequestParam("text") String text) {
        log.info("Получен HTTP-запрос на поиск вещи по тексту {}", text);
        return itemService.search(text).stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }
}
