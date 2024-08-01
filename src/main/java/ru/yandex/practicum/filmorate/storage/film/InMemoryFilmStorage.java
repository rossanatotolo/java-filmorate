package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 0;

    @Override   //получение списка фильмов
    public Collection<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override // для добавления нового фильма в список.
    public Film filmCreate(final Film film) {
        film.setId(getIdNext());
        films.put(film.getId(), film);
        return film;
    }

    @Override //для обновления данных существующего фильма.
    public Film filmUpdate(final Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override //получение фильма по id
    public Optional<Film> getFilmById(final Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override //добавление лайка
    public Set<Long> addLike(final Long id, final Long idUser) {
        films.get(id).getLikes().add(idUser);
        return films.get(id).getLikes();
    }

    @Override // удаление лайка
    public Set<Long> deleteLike(final Long id, final Long idUser) {
        films.get(id).getLikes().remove(idUser);
        return films.get(id).getLikes();
    }

    @Override // получение списка лучших фильмов
    public List<Film> getPopular(final Long count) {
        return films.values().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private long getIdNext() {
        return ++currentId;
    }
}
