package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFound(UserNotFoundException e) {
        log.debug("Пользователь не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleItemNotFound(ItemNotFoundException e) {
        log.debug("Вещь не найдена: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleBookingNotFound(BookingNotFoundException e) {
        log.debug("Бронь не найдена: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(ItemRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleItemRequestNotFound(ItemRequestNotFoundException e) {
        log.debug("Запрос не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExists(AlreadyExistsException e) {
        log.debug("Уже существует: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.CONFLICT.value())
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDenied(AccessDeniedException e) {
        log.debug("У пользователя нет прав для этого действия: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.FORBIDDEN.value())
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        log.debug("Ошибка валидации: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .error(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Ошибка валидации: {}", message);
        return ApiError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .error(message)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        log.error("Ошибка: {}", e.getMessage());
        return ApiError.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(e.getMessage())
                .build();
    }
}
