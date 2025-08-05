package ru.practicum.shareit.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {
    private String error;
    private Integer errorCode;
}
