package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequestResponse createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemRequestDto requestDto) {
        log.info("Получен HTTP-запрос на создание запроса: {}", requestDto);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(userId, requestDto);
        log.info("Успешно обработан запрос на создание запроса: {}", itemRequest);
        return itemRequestMapper.toItemRequestResponse(itemRequestService.create(userId, itemRequest));
    }

    @GetMapping
    public List<ItemRequestResponse> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP-запрос на получение всех запросов пользователя с id: {}", userId);
        return itemRequestService.getUserRequests(userId).stream()
                .map(itemRequestMapper::toItemRequestResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<ItemRequestResponse> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP-запрос на получение всех запросов от пользователя с id: {}", userId);
        return itemRequestService.getAllRequests(userId).stream()
                .map(itemRequestMapper::toItemRequestResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponse getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long requestId) {
        log.info("Получен HTTP-запрос на получение запроса с id: {}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }
}
