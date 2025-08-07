package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestResponse {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private List<ItemResponse> items;
}