package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.DateReleaseValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    @NotBlank(message = "Фильм должен быть указан")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    @NotBlank
    private String description;
    @DateReleaseValidation
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private Long duration;
    private final Set<Long> likes = new HashSet<>();
}
