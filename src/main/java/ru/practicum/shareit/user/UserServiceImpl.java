package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        validateEmailExists(user.getEmail());
        return userRepository.create(user);
    }

    @Override
    public User update(Long userId, User user) {
        validateEmailExists(user.getEmail());
        return userRepository.update(userId, user);
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userId);
    }

    @Override
    public User getById(long id) {
        User user = userRepository.getById(id);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    private void validateEmailExists(String email) {
        List<String> emails = userRepository.getAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (emails.contains(email)) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует");
        }
    }
}

