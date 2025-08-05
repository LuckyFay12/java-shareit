package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemCreateRequest request) {
        log.info("Получен HTTP-запрос на добавление вещи: {}", request);
        return itemClient.create(userId, request);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Min(1L) @PathVariable Long itemId,
                                         @Valid @RequestBody ItemUpdateRequest request) {
        log.info("Получен HTTP-запрос на обновление вещи с id {}", itemId);
        return itemClient.update(itemId, userId, request);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Min(1L) @PathVariable Long itemId) {
        log.info("Получен HTTP-запрос на получение вещи с id {}", itemId);
        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP-запрос на получение всех вещей пользователя id {}", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String text) {
        log.info("Получен HTTP-запрос на поиск вещи по тексту {}", text);
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Min(1) @PathVariable Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody CommentCreateRequest request) {
        log.info("Получен HTTP-запрос на добавление комментария к вещи c id {}", itemId);
        return itemClient.createComment(userId, itemId, request);
    }
}

