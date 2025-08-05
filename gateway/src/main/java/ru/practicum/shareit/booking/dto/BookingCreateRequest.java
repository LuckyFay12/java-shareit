package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {
    @Null
    private Long id;
    @NotNull
    @FutureOrPresent(message = "Дата начала не может быть в прошлом")
    private LocalDateTime start;
    @NotNull
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}
