package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    @Null
    private Long id;
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}
