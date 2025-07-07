package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.catalina.connector.Request;
import ru.practicum.shareit.user.User;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemUpdateRequest {
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Request request;
}