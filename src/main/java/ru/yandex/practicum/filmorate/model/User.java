package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @NotBlank(message = "Имейл должен быть указан")
    @Email(message = "Имейл должен содержать символ «@». Формат имейла: example@mail.com")
    private String email;
    @NotBlank(message = "Логин должен быть указан")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,300}$",
            message = "Логин должен иметь длину от 1 до 300 без специальных символов и пробелов")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть задана в будущем")
    @NotNull
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}
