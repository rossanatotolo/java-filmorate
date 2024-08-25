package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImp implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public Genre getGenreById(final int id) {
        log.info("Получение жанра по id.");
        return genreStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id: " + id + " не существует."));
    }

    @Override
    public List<Genre> getAllGenres() {
        log.info("Получение списка всех жанров.");
        return genreStorage.getAllGenres();
    }
}
