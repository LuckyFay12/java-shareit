package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlreadyExistsExceptionTest {
    @Test
    void whenExceptionThrown_thenMessageIsCorrect() {
        String errorMessage = "Уже существует";

        try {
            throw new AlreadyExistsException(errorMessage);
        } catch (AlreadyExistsException e) {
            assertEquals(errorMessage, e.getMessage(),
                    "Сообщение в пойманном исключении должно соответствовать переданному в конструктор");
        }
    }
}

