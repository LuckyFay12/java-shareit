package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccessDeniedExceptionTest {
    @Test
    void whenExceptionThrown_thenMessageIsCorrect() {
        String errorMessage = "Доступ ограничен";

        try {
            throw new AccessDeniedException(errorMessage);
        } catch (AccessDeniedException e) {
            assertEquals(errorMessage, e.getMessage(),
                    "Сообщение в пойманном исключении должно соответствовать переданному в конструктор");
        }
    }
}

