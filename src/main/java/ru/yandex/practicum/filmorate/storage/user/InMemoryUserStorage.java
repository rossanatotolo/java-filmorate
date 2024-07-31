package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 0;

    @Override //получение списка пользователей.
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override // для добавления нового пользователя в список.
    public User userCreate(final User user) {
        user.setId(getIdNext());
        users.put(user.getId(), user);
        return user;
    }

    @Override //для обновления данных существующего пользователя.
    public User userUpdate(final User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override // получение пользователя по id
    public Optional<User> getUserById(final Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Set<Long> addNewFriend(final Long idUser, final Long idFriend) { //добавление пользователя в друзья
        final Set<Long> userFriends = users.get(idUser).getFriends();
        userFriends.add(idFriend);
        users.get(idFriend).getFriends().add(idUser);
        return userFriends;
    }

    @Override
    public Set<Long> deleteFriend(final Long idUser, final Long idFriend) { // удаление из друзей пользователя
        final Set<Long> userFriends = users.get(idUser).getFriends();
        userFriends.remove(idFriend);
        users.get(idFriend).getFriends().remove(idUser);
        return userFriends;
    }

    @Override
    public List<User> getAllFriends(final Long idUser) { // получение списка друзей пользователя
        return users.get(idUser).getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(final Long idUser, final Long idOther) { // получение списка общих друзей с пользователем
        Set<Long> user = users.get(idUser).getFriends();
        Set<Long> userOther = users.get(idOther).getFriends();
        return user.stream()
                .filter(userOther::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }

    private long getIdNext() {
        return ++currentId;
    }
}
