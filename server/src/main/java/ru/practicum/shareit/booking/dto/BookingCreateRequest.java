package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
