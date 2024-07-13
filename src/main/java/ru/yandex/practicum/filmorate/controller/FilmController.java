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
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    LocalDate minRealiseData = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping //   получение списка фильмов
    public Collection<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return films.values();
    }

    @PostMapping() // для добавления нового фильма в список.
    public Film filmCreate(@Valid @RequestBody Film film) { // значение, которое будет передано в метод в качестве аргумента, нужно взять из тела запроса
        log.info("Поступил Post запрос /films с телом {}", film);
        if (films.containsValue(film)) { // существует ли
            log.info("Фильм уже добавлен в список");
            throw new DuplicatedDataException("Фильм уже добавлен");
        }

        filmValidate(film);
        // формируем дополнительные данные
        film.setId(getIdNext());
        // сохраняем нового пользователя в памяти приложения
        films.put(film.getId(), film);
        log.info("Отправлен ответ Post /films с телом {}", film);
        return film;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getIdNext() { //Он находит max идентификатор среди уже добавленных публикаций и увеличивает его на единицу.
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping() //для обновления данных существующего фильма.
    public Film filmUpdate(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с id: " + film.getId() + " не найден");
        }

        filmValidate(film);
        log.info("Поступил Put запрос /films с телом {}", film);
        films.put(film.getId(), film);
        log.info("Отправлен ответ Put /films с телом {}", film);
        return film;
    }

    private void filmValidate(Film film) {
        if (film.getName() == null) {
            log.info("Ошибка в названии фильма");
            throw new ValidationException("Название фильма задано некорректно");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.info("Превышено допустимое количество символов");
            throw new ValidationException("Максимальная длина описани - 200 символов");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(minRealiseData)) {
            log.info("Недопустимая дата релиза");
            throw new ValidationException("Дата релиза должна быть после 28.12.1895");
        }
    }
}

