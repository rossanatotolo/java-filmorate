package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

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
    public User getUserById(@PathVariable @Positive final int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}") //добавление пользователя в друзья
    public void addNewFriend(@PathVariable("id") @Positive final int userId, @PathVariable @Positive final int friendId) {
        userService.addNewFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") // удаление из друзей пользователя
    public void deleteFriend(@PathVariable("id") @Positive final int userId, @PathVariable @Positive final int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends") // получение списка друзей пользователя
    public List<User> getAllFriends(@PathVariable("id") @Positive final int userId) {
        return userService.getAllFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // получение списка общих друзей с пользователем
    public List<User> getCommonFriends(@PathVariable("id") @Positive final int userId, @PathVariable @Positive final int otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}

