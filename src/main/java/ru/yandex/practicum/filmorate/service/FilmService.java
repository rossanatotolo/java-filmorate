package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Collection<Film> getAllFilms() { //   получение списка фильмов
        log.info("Получение списка всех фильмов.");
        return filmStorage.getAllFilms();
    }

    public Film filmCreate(final Film film) { // для добавления нового фильма в список.
        if (filmStorage.getAllFilms().contains(film)) {
            log.warn("Фильм с id {} уже добавлен в список.", film.getId());
            throw new DuplicatedDataException("Фильм уже добавлен.");
        }

        Film film1 = filmStorage.filmCreate(film);
        log.info("Фильм с id {} добавлен в список.", film.getId());
        return film1;
    }

    public Film filmUpdate(final Film film) { //для обновления данных существующего фильма.
        if (filmStorage.getFilmById(film.getId()).isEmpty()) {
            log.warn("Фильм с id {} не найден.", film.getId());
            throw new NotFoundException("Фильм с id: " + film.getId() + " не найден.");
        }

        Film film1 = filmStorage.filmUpdate(film);
        log.info("Фильм с id {} обновлен.", film.getId());
        return film1;
    }

    public Film getFilmById(final Long id) {
        log.info("Получение фильма по id.");
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id: " + id + " не существует."));
    }

    public Set<Long> addLike(final Long id, final Long idUser) { //добавление лайка
        getFilmById(id);
        userService.getUserById(idUser);
        if (filmStorage.getFilmById(id).get().getLikes().contains(idUser)) {
            log.warn("Ошибка при добавлении лайка. Пользователь уже поставил лайк.");
            throw new ValidationException("Ошибка при добавлении лайка. Пользователь уже поставил лайк.");
        }
        Set<Long> setLikes = filmStorage.addLike(id, idUser);
        log.info("Пользователь с id {} добавил лайк к фильму с id {}.", idUser, id);
        return setLikes;
    }

    public Set<Long> deleteLike(final Long id, final Long idUser) { // удаление лайка
        getFilmById(id);
        userService.getUserById(idUser);
        Set<Long> setLikes = filmStorage.deleteLike(id, idUser);
        log.info("Пользователь с id {} удалил лайк к фильму с id {}.", idUser, id);
        return setLikes;
    }

    public List<Film> getPopular(final Long count) { // получение списка лучших фильмов
        if (filmStorage.getAllFilms().isEmpty()) {
            log.warn("Ошибка при получении списка фильмов. Список фильмов пуст.");
            throw new NotFoundException("Ошибка при получении списка фильмов. Список фильмов пуст.");
        }
        if (filmStorage.getAllFilms().size() < count) {
            return filmStorage.getPopular((long) filmStorage.getAllFilms().size());
        }
        return filmStorage.getPopular(count);
    }
}
