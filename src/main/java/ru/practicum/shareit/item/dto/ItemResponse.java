package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.connector.Request;
import ru.practicum.shareit.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Request request;
}