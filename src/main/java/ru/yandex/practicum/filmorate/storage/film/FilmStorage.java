package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {
    Collection<Film> getAllFilms(); // получение списка всех фильмов

    Film filmCreate(Film film); // добавление фильма

    Film filmUpdate(Film film); //обновление фильма

    Optional<Film> getFilmById(Long id); //получение фильма по id

    Set<Long> addLike(Long id, Long idUser); //добавление лайка

    Set<Long> deleteLike(Long id, Long idUser); // удаление лайка

    List<Film> getPopular(Long count); // получение списка лучших фильмов
}


