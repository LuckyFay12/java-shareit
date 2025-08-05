package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserCreateRequest request) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", request);
        return userClient.create(request);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable @Positive Long userId,
                                         @Valid @RequestBody UserUpdateRequest request) {
        log.info("Получен HTTP-запрос на обновление пользователя с id {}", userId);
        return userClient.update(userId, request);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@Min(1L) @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на получение пользователя с id {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET /users - получение всех пользователей");
        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long userId) {
        log.info("Получен HTTP-запрос на удаление пользователя с id {}", userId);
        return userClient.delete(userId);
    }
}