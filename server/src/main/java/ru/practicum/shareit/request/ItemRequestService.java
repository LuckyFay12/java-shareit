package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.util.List;

public interface ItemRequestService {

    ItemRequest create(Long userId, ItemRequest request);

    List<ItemRequest> getUserRequests(Long userId);

    List<ItemRequest> getAllRequests(Long userId);

    ItemRequestResponse getRequestById(Long userId, Long requestId);
}
