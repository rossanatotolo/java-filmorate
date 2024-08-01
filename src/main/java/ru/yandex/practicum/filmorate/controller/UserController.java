package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping //получение списка пользователей.
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping() // для добавления нового пользователя в список.
    @ResponseStatus(HttpStatus.CREATED)
    public User userCreate(@Valid @RequestBody final User user) { // значение, которое будет передано в метод в качестве аргумента, нужно взять из тела запроса
        return userService.userCreate(user);
    }

    @PutMapping() //для обновления данных существующего пользователя.
    public User userUpdate(@Valid @RequestBody final User user) {
        return userService.userUpdate(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable @Positive final Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}") //добавление пользователя в друзья
    public Set<Long> addNewFriend(@PathVariable("id") @Positive final Long idUser, @PathVariable("friendId") @Positive final Long idFriend) {
        return userService.addNewFriend(idUser, idFriend);
    }

    @DeleteMapping("/{id}/friends/{friendId}") // удаление из друзей пользователя
    public Set<Long> deleteFriend(@PathVariable("id") @Positive final Long idUser, @PathVariable("friendId") @Positive final Long idFriend) {
        return userService.deleteFriend(idUser, idFriend);
    }

    @GetMapping("/{id}/friends") // получение списка друзей пользователя
    public List<User> getAllFriends(@PathVariable("id") @Positive final Long idUser) {
        return userService.getAllFriends(idUser);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // получение списка общих друзей с пользователем
    public List<User> getCommonFriends(@PathVariable("id") @Positive final Long idUser, @PathVariable("otherId") @Positive final Long idOther) {
        return userService.getCommonFriends(idUser, idOther);
    }
}

