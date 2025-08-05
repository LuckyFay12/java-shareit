package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    private Long id;
    private String text;
}
