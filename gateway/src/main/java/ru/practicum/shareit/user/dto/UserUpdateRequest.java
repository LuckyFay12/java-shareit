package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    @Email(message = "Неверный формат Email")
    private String email;
}
