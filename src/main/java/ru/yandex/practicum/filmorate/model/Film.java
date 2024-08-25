package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.DateReleaseValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(exclude = {"id"})
public class Film {
    private Integer id;
    @Size(max = 255, message = "Максимальная длина - 255 символов")
    @NotBlank(message = "Фильм должен быть указан")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    @NotBlank
    private String description;
    @DateReleaseValidation
    @NotNull
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private int duration;
    private final Set<Long> likes = new HashSet<>();
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
}
