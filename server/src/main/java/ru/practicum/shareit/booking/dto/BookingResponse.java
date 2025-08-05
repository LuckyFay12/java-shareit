package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.user.dto.UserResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserResponse booker;
    private ItemResponse item;
    private Status status;
}
