package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (users.values().stream()
                .anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ConditionsNotMetException("Имя пользователя должно быть указано");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ConditionsNotMetException("Пароль должен быть указан");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
                throw new ConditionsNotMetException("Имейл не может быть пустым");
            }
            if (!newUser.getEmail().equals(oldUser.getEmail()) &&
                    users.values().stream()
                            .anyMatch(user -> user.getEmail().equals(newUser.getEmail()))) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());
            oldUser.setEmail(newUser.getEmail());
            return oldUser;
        }
        throw new NotFoundException("Пост с id = " + newUser.getId() + " не найден");
    }

    public User findUserById(long id) {
        Optional<User> opUser = users.values()
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst();
        if (opUser.isPresent()) {
            return opUser.get();
        } else {
            throw new NotFoundException("User с id = " + id + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
