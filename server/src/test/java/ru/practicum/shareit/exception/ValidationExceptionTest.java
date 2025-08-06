package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationExceptionTest {
    @Test
    void whenExceptionThrown_thenMessageIsCorrect() {
        String errorMessage = "Ошибка валидации";

        try {
            throw new ValidationException(errorMessage);
        } catch (ValidationException e) {
            assertEquals(errorMessage, e.getMessage(),
                    "Сообщение в пойманном исключении должно соответствовать переданному в конструктор");
        }
    }
}

