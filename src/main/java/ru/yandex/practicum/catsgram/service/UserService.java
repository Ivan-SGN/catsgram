package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create( User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        validateEmail(user);
        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(newUser.getPassword());
            if (newUser.getEmail() != null){
                validateEmail(newUser);
                oldUser.setEmail(newUser.getEmail());
            }
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public Optional<User> findById(long authorId) {
        return Optional.ofNullable(users.get(authorId));
    }


    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateEmail(User user){
        boolean emailAlreadyExists =  users.values().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()));
        if (emailAlreadyExists) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
    }

    public Optional<User> findUserById(Long id){
        return Optional.ofNullable(users.get(id));
    }
}
