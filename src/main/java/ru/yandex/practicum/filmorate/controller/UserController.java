package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping //получение списка пользователей.
    public Collection<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return users.values();
    }

    @PostMapping() // для добавления нового пользователя в список.
    public User userCreate(@Valid @RequestBody User user) { // значение, которое будет передано в метод в качестве аргумента, нужно взять из тела запроса
        log.info("Поступил Post запрос /users с телом {}", user);
        if (users.containsValue(user)) {
            log.warn("Пользователь уже добавлен в список");
            throw new DuplicatedDataException("Этот пользователь уже существует");
        }
        userValidate(user);
        // формируем дополнительные данные
        user.setId(getIdNext());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        log.info("Отправлен ответ Post /users с телом {}", user);
        return user;
    }

    @PutMapping() //для обновления данных существующего пользователя.
    public User userUpdate(@Valid @RequestBody User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Пользователь с id: " + user.getId() + " не найден");
        }

        userValidate(user);
        log.info("Поступил Put запрос /users с телом {}", user);
        users.put(user.getId(), user);
        log.info("Отправлен ответ Put /users с телом {}", user);
        return user;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getIdNext() { //Он находит max идентификатор среди уже добавленных публикаций и увеличивает его на единицу.
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void userValidate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка в написании даты рождения");
            throw new ValidationException("Дата рождения не может быть задана в будущем");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

