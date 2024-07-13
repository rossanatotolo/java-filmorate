package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    Long id;
    @NotBlank(message = "Фильм должен быть указан")
    String name; //название фильма
    @NotBlank(message = "Описание должно быть указано")
    String description; //описание фильма
    LocalDate releaseDate; // дата релиза
    @Min(value = 0, message = "Продолжительность фильма должна быть положительным числом")
    Long duration; //продолжительность фильма
}
