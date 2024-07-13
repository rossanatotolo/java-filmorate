package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController;
    Film film;
    MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = new Film();
        mockMvc = MockMvcBuilders.standaloneSetup(new FilmController()).build();
    }

    @Test
    @DisplayName("Проверка на добавление фильма")
    void shouldAddFilm() {
        film.setName("Гарри Поттер и Кубок огня");
        film.setDescription("Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы чародейства и волшебства Хогвартс.");
        film.setReleaseDate(LocalDate.of(2005, 11, 6));
        film.setDuration(157L);

        filmController.filmCreate(film);
        assertEquals(film.getId(), 1, "Фильм не добавлен");
    }

    @Test
    @DisplayName("Проверка на пустой id у фильма")
    void shouldIdFilmIsEmpty() {
        film.setId(null);
        film.setName("Гарри Поттер и Кубок огня");
        film.setDescription("Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы чародейства и волшебства Хогвартс.");
        film.setReleaseDate(LocalDate.of(2005, 11, 6));
        film.setDuration(157L);

        assertThrows(NotFoundException.class, () -> {
            filmController.filmUpdate(film);
        });
    }

    @Test
    @DisplayName("Проверка на обновление фильма")
    void shouldUpdateFilm() {
        film.setName("Гарри Поттер и Кубок огня");
        film.setDescription("Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы чародейства и волшебства Хогвартс.");
        film.setReleaseDate(LocalDate.of(2005, 11, 6));
        film.setDuration(157L);

        filmController.filmCreate(film); //1

        Film film1 = new Film();
        film1.setName("Гарри Поттер и Орден Феникса");
        film1.setDescription("Гарри проводит свой пятый год в школе Хогвартс.");
        film1.setReleaseDate(LocalDate.of(2007, 6, 28));
        film1.setDuration(138L);
        film1.setId(1L);

        filmController.filmUpdate(film1);
        assertEquals(film1.getDuration(), 138, "Фильм не обновлен");
        assertEquals(filmController.getAllFilms().size(), 1, "Количество фильмов не совпадает");
    }

    @Test
    @DisplayName("Проверка получение списка фильмов")
    void shouldGetAllFilms() {
        film.setName("Гарри Поттер и Кубок огня");
        film.setDescription("Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы чародейства и волшебства Хогвартс.");
        film.setReleaseDate(LocalDate.of(2005, 11, 6));
        film.setDuration(157L);
        filmController.filmCreate(film);

        Film film1 = new Film();
        film1.setName("Гарри Поттер и Орден Феникса");
        film1.setDescription("Гарри проводит свой пятый год в школе Хогвартс.");
        film1.setReleaseDate(LocalDate.of(2007, 6, 28));
        film1.setDuration(138L);
        filmController.filmCreate(film1);

        Film film2 = new Film();
        film2.setName("Гарри Поттер и Принц-полукровка");
        film2.setDescription("Теперь не только мир волшебников, но и мир маглов ощущает на себе силу Волан-де-Морта.");
        film2.setReleaseDate(LocalDate.of(2009, 7, 6));
        film2.setDuration(153L);
        filmController.filmCreate(film2);

        assertEquals(filmController.getAllFilms().size(), 3, "Количество фильмов не совпадает");
    }

    @Test
    @DisplayName("Проверка на валидацию фильма с пустым названием")
    void shouldValidationFilmForEmptyName() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\" \","
                                + "\"description\":\"Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы чародейства и волшебства Хогвартс.\","
                                + "\"releaseDate\":\"2005-11-6\",\"duration\":157}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка на валидацию фильма с описанием, превышающим максимально допустимую длину")
    void shouldValidationFilmForMaxLengthForDescription() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Гарри Поттер и Кубок огня.\","
                                + "\"description\":\"Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы " +
                                "чародейства и волшебства Хогвартс. При таинственных обстоятельствах Гарри был отобран " +
                                "в число участников опасного соревнования — Турнира Трёх Волшебников\","
                                + "\"releaseDate\":\"2005-11-6\",\"duration\":157}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Проверка на валидацию фильма с некорректной датой релиза")
    void shouldValidationFilmForIncorrectReleaseDate() {
        film.setName("Гарри Поттер и Кубок огня");
        film.setDescription("Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы чародейства и волшебства Хогвартс.");
        film.setReleaseDate(LocalDate.of(1500, 11, 6));
        film.setDuration(157L);
        assertThrows(ValidationException.class, () -> {
            filmController.filmCreate(film);
        });
    }

    @Test
    @DisplayName("Проверка на валидацию фильма с отрицательной продолжительностью")
    void shouldValidationFilmANegativeDuration() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Гарри Поттер и Кубок огня.\","
                                + "\"description\":\"Гарри Поттер, Рон и Гермиона возвращаются на четвёртый курс школы чародейства и волшебства Хогвартс.\","
                                + "\"releaseDate\":\"2005-11-6\",\"duration\":-1}"))
                .andExpect(status().isBadRequest());
    }
}
