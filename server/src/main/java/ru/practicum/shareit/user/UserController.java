package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest request) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", request);
        User createdUser = userService.create(userMapper.toUser(request));
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", createdUser);
        return userMapper.toUserResponse(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserResponse update(@PathVariable Long userId,
                               @RequestBody UserUpdateRequest request) {
        log.info("Получен HTTP-запрос на обновление пользователя с id {}", userId);
        User updatedUser = userService.update(userId, userMapper.toUser(request));
        log.info("Успешно обработан HTTP-запрос на обновление пользователя с id {}", updatedUser.getId());
        return userMapper.toUserResponse(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Получен HTTP-запрос на удаление пользователя с id {}", userId);
        userService.delete(userId);
    }

    @GetMapping("/{userId}")
    public UserResponse get(@PathVariable Long userId) {
        log.info("Получен HTTP-запрос на получение пользователя с id {}", userId);
        return userMapper.toUserResponse(userService.getById(userId));
    }

    @GetMapping
    public List<UserResponse> getAll() {
        log.info("Получен HTTP-запрос на получение списка всех пользователей");
        return userService.getAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
}
