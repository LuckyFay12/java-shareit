package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemRequestMapper itemRequestMapper;


    @Override
    public ItemRequest create(Long userId, ItemRequest request) {
        userService.getById(userId);
        request.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(request);
    }

    @Override
    public List<ItemRequest> getUserRequests(Long userId) {
        userService.getById(userId);
        return itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId);
    }

    @Override
    public List<ItemRequest> getAllRequests(Long userId) {
        userService.getById(userId);
        return itemRequestRepository.findAllOtherUsersRequests(userId);
    }

    @Override
    public ItemRequestResponse getRequestById(Long userId, Long requestId) {
        userService.getById(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Запрос не найден"));
        ItemRequestResponse itemRequestResponse = itemRequestMapper.toItemRequestResponse(itemRequest);
        List<ItemResponse> items = itemRepository.findAllByItemRequest(itemRequest).stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
        itemRequestResponse.setItems(items);
        return itemRequestResponse;
    }
}
