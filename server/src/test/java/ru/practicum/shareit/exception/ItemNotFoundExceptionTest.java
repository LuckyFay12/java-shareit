package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemNotFoundExceptionTest {
    @Test
    void whenExceptionThrown_thenMessageIsCorrect() {
        String errorMessage = "Вещь не найдена";

        try {
            throw new ItemNotFoundException(errorMessage);
        } catch (ItemNotFoundException e) {
            assertEquals(errorMessage, e.getMessage(),
                    "Сообщение в пойманном исключении должно соответствовать переданному в конструктор");
        }
    }
}

