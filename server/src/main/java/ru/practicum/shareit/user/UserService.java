package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(Long userId, User user);

    void delete(Long userId);

    User getById(Long id);

    List<User> getAll();
}
