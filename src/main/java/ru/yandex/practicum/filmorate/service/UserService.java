package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAllUsers() { //получение списка пользователей.
        log.info("Получение списка всех пользователей");
        return userStorage.getAllUsers();
    }

    public User userCreate(final User user) { // для добавления нового пользователя в список.
        if (userStorage.getAllUsers().contains(user)) {
            log.warn("Пользователь с id {} уже добавлен в список.", user.getId());
            throw new DuplicatedDataException("Этот пользователь уже существует.");
        }
        userValidate(user);
        User user1 = userStorage.userCreate(user);
        log.info("Пользователь с id {} добавлен.", user.getId());
        return user1;
    }

    public User userUpdate(final User user) { //для обновления данных существующего пользователя.
        if (user.getId() == null || userStorage.getUserById(user.getId()).isEmpty()) {
            log.warn("Пользователь с id {} не найден.", user.getId());
            throw new NotFoundException("Пользователь с id: " + user.getId() + " не найден.");
        }
        userValidate(user);
        User user1 = userStorage.userUpdate(user);
        log.info("Пользователь с id {} обновлен.", user.getId());
        return user1;
    }

    public User getUserById(final Long id) { // получение пользователя по id
        log.info("Получение пользователя по id.");
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден."));
    }

    public Set<Long> addNewFriend(final Long idUser, final Long idFriend) { //добавление пользователя в друзья
        getUserById(idUser);
        getUserById(idFriend);
        if (idUser.equals(idFriend)) {
            log.warn("Ошибка при добавлении в друзья. Id пользователей совпадают.");
            throw new ValidationException("Ошибка при добавлении в друзья. Id пользователей совпадают.");
        }
        Set<Long> setFriends = userStorage.addNewFriend(idUser, idFriend);
        log.info("Пользователь с id {} добавлен в друзья к пользователю с id {}.", idFriend, idUser);
        return setFriends;
    }

    public Set<Long> deleteFriend(final Long idUser, final Long idFriend) { // удаление из друзей пользователя
        getUserById(idUser);
        getUserById(idFriend);
        if (idUser.equals(idFriend)) {
            log.warn("Ошибка при удалении пользователя из друзей. Id пользователей совпадают.");
            throw new ValidationException("Ошибка при удалении пользователя из друзей. Id пользователей совпадают.");
        }
        Set<Long> setFriends = userStorage.deleteFriend(idUser, idFriend);
        log.info("Пользователь с id {} удален из друзей пользователя с id {}.", idFriend, idUser);
        return setFriends;
    }

    public List<User> getAllFriends(final Long idUser) { // получение списка друзей пользователя
        getUserById(idUser);
        List<User> listFriends = userStorage.getAllFriends(idUser);
        log.info("Получение списка друзей пользователя с id {}.", idUser);
        return listFriends;
    }

    public List<User> getCommonFriends(final Long idUser, final Long idOther) { // получение списка общих друзей с пользователем
        getUserById(idUser);
        getUserById(idOther);
        if (idUser.equals(idOther)) {
            log.warn("Ошибка при получении списка общих друзей. Id пользователей совпадают.");
            throw new ValidationException("Ошибка при получении списка общих друзей. Id пользователей совпадают.");
        }
        List<User> listFriends = userStorage.getCommonFriends(idUser, idOther);
        log.info("Получение списка общих друзей у пользователя с id {} и пользователя с id {}.", idOther, idUser);
        return listFriends;
    }

    private void userValidate(final User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
