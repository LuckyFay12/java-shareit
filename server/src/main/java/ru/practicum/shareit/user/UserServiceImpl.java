package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        validateEmailExists(user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User update(Long userId, User user) {
        validateEmailExists(user.getEmail());
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        return userRepository.save(updatedUser);
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    private void validateEmailExists(String email) {
        List<String> emails = userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (emails.contains(email)) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует");
        }
    }
}

