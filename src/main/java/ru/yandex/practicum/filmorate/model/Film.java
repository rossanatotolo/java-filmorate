package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.DateReleaseValidation;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    @NotBlank(message = "Фильм должен быть указан")
    private String name; //название фильма
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description; //описание фильма
    @DateReleaseValidation
    private LocalDate releaseDate; // дата релиза
    @Min(value = 0, message = "Продолжительность фильма должна быть положительным числом")
    private Long duration; //продолжительность фильма
}
