package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class UserRepository {

    private Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    public User create(User user) {
        user.setId(idCounter++);
        users.put(user.getId(), user);
        return user;
    }

    public User update(Long userId, User user) {
        User stored = users.get(userId);

        if (Objects.isNull(stored)) {
            return null;
        }
        if (Objects.nonNull(user.getName()) && !Objects.equals(stored.getName(), user.getName())) {
            stored.setName(user.getName());
        }
        if (Objects.nonNull(user.getEmail()) && !Objects.equals(stored.getEmail(), user.getEmail())) {
            stored.setEmail(user.getEmail());
        }
        return stored;
    }

    public void delete(Long id) {
            users.remove(id);
        }


    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getById(Long id) {
        return users.get(id);
    }
}
