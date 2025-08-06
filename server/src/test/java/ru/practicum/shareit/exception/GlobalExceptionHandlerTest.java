package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.file.AccessDeniedException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void handleUserNotFound_ShouldReturnNotFoundResponse() {
        UserNotFoundException ex = new UserNotFoundException("Пользователь не найден");

        ApiError response = exceptionHandler.handleUserNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getErrorCode());
        assertEquals("Пользователь не найден", response.getError());
    }

    @Test
    void handleItemNotFound_ShouldReturnNotFoundResponse() {
        ItemNotFoundException ex = new ItemNotFoundException("Вещь не найдена");

        ApiError response = exceptionHandler.handleItemNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getErrorCode());
        assertEquals("Вещь не найдена", response.getError());
    }

    @Test
    void handleBookingNotFound_ShouldReturnNotFoundResponse() {
        BookingNotFoundException ex = new BookingNotFoundException("Бронь не найдена");

        ApiError response = exceptionHandler.handleBookingNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getErrorCode());
        assertEquals("Бронь не найдена", response.getError());
    }

    @Test
    void handleItemRequestNotFound_ShouldReturnNotFoundResponse() {
        ItemRequestNotFoundException ex = new ItemRequestNotFoundException("Запрос не найден");

        ApiError response = exceptionHandler.handleItemRequestNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getErrorCode());
        assertEquals("Запрос не найден", response.getError());
    }

    @Test
    void handleAlreadyExists_ShouldReturnConflictResponse() {
        AlreadyExistsException ex = new AlreadyExistsException("Уже существует");

        ApiError response = exceptionHandler.handleAlreadyExists(ex);

        assertEquals(HttpStatus.CONFLICT.value(), response.getErrorCode());
        assertEquals("Уже существует", response.getError());
    }

    @Test
    void handleAccessDenied_ShouldReturnForbiddenResponse() {
        AccessDeniedException ex = new AccessDeniedException("Доступ запрещен");

        ApiError response = exceptionHandler.handleAccessDenied(ex);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getErrorCode());
        assertEquals("Доступ запрещен", response.getError());
    }

    @Test
    void handleValidationException_ShouldReturnBadRequestResponse() {
        ValidationException ex = new ValidationException("Validation error");

        ApiError response = exceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getErrorCode());
        assertEquals("Validation error", response.getError());
    }

    @Test
    void handleMethodArgumentNotValid_ShouldReturnBadRequestWithMessages() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "default message");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ApiError response = exceptionHandler.handleNotValid(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getErrorCode());
        assertEquals("default message", response.getError());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");

        ApiError response = exceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getErrorCode());
        assertEquals("Unexpected error", response.getError());
    }
}