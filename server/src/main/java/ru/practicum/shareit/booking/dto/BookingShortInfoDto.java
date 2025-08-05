package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingShortInfoDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
}
