package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @GetMapping //   получение списка фильмов
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping() // для добавления нового фильма в список.
    @ResponseStatus(HttpStatus.CREATED)
    public Film filmCreate(@Valid @RequestBody final Film film) { // значение, которое будет передано в метод в качестве аргумента, нужно взять из тела запроса
        return filmService.filmCreate(film);
    }

    @PutMapping() //для обновления данных существующего фильма.
    public Film filmUpdate(@Valid @RequestBody final Film film) {
        return filmService.filmUpdate(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable @Positive @RequestBody final Long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}") //добавление лайка
    public Set<Long> addLike(@PathVariable @Positive final Long id, @PathVariable("userId") @Positive final Long idUser) {
        return filmService.addLike(id, idUser);
    }

    @DeleteMapping("/{id}/like/{userId}") //удаление лайка
    public Set<Long> deleteLike(@PathVariable @Positive final Long id, @PathVariable("userId") @Positive final Long idUser) {
        return filmService.deleteLike(id, idUser);
    }

    @GetMapping("/popular")  // получение списка лучших фильмов
    public List<Film> getPopular(@RequestParam(defaultValue = "10") @Positive final Long count) {
        return filmService.getPopular(count);
    }
}

