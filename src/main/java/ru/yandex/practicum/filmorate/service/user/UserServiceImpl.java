package ru.yandex.practicum.filmorate.service.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public Collection<User> getAllUsers() { //получение списка пользователей.
        log.info("Получение списка всех пользователей");
        return userStorage.getAllUsers();
    }

    @Override
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

    @Override
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

    @Override
    public User getUserById(final int id) { // получение пользователя по id
        log.info("Получение пользователя по id.");
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден."));
    }

    @Override
    public void addNewFriend(final int idUser, final int idFriend) { //добавление пользователя в друзья
        getUserById(idUser);
        getUserById(idFriend);
        if (idUser == idFriend) {
            log.warn("Ошибка при добавлении в друзья. Id пользователей совпадают.");
            throw new ValidationException("Ошибка при добавлении в друзья. Id пользователей совпадают.");
        }
        userStorage.addNewFriend(idUser, idFriend);
        log.info("Пользователь с id {} добавлен в друзья к пользователю с id {}.", idFriend, idUser);
    }

    @Override
    public void deleteFriend(final int idUser, final int idFriend) { // удаление из друзей пользователя
        getUserById(idUser);
        getUserById(idFriend);
        if (idUser == idFriend) {
            log.warn("Ошибка при удалении пользователя из друзей. Id пользователей совпадают.");
            throw new ValidationException("Ошибка при удалении пользователя из друзей. Id пользователей совпадают.");
        }
        userStorage.deleteFriend(idUser, idFriend);
        log.info("Пользователь с id {} удален из друзей пользователя с id {}.", idFriend, idUser);
    }

    @Override
    public List<User> getAllFriends(final int idUser) { // получение списка друзей пользователя
        getUserById(idUser);
        List<User> listFriends = userStorage.getAllFriends(idUser);
        log.info("Получение списка друзей пользователя с id {}.", idUser);
        return listFriends;
    }

    @Override
    public List<User> getCommonFriends(final int idUser, final int idOther) { // получение списка общих друзей с пользователем
        getUserById(idUser);
        getUserById(idOther);
        if (idUser == idOther) {
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
