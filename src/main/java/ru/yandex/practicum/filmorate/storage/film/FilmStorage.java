package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {
    Collection<Film> getAllFilms(); // получение списка всех фильмов

    Film filmCreate(Film film); // добавление фильма

    Film filmUpdate(Film film); //обновление фильма

    Optional<Film> getFilmById(int id); //получение фильма по id

    void addLike(int id, int idUser); //добавление лайка

    void deleteLike(int id, int idUser); // удаление лайка

    List<Film> getPopular(int count); // получение списка лучших фильмов

    List<Integer> getAllId();
}


