package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<User> getAllUsers();

    User userCreate(User user);

    User userUpdate(User user);

    User getUserById(int id);

    void addNewFriend(int idUser, int idFriend);

    void deleteFriend(int idUser, int idFriend);

    List<User> getAllFriends(int idUser);

    List<User> getCommonFriends(int idUser, int idOther);
}
