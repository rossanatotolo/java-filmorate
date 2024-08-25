package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaServiceImp implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public Mpa getMpaById(final int id) {
        log.info("Получение рейтинга по id.");
        return mpaStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id: " + id + " не существует."));
    }

    @Override
    public List<Mpa> getAllMpa() {
        log.info("Получение списка всех рейтингов.");
        return mpaStorage.getAllMpa();
    }
}
