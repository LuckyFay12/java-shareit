package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortInfoDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private String ownerName;
    private Long requestId;
    private List<CommentResponse> comments;
    private BookingShortInfoDto lastBooking;
    private BookingShortInfoDto nextBooking;
}