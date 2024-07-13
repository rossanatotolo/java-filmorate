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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {
    UserController userController;
    User user;
    MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user = new User();
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }

    @Test
    @DisplayName("Проверка на добавление пользователя")
    void shouldAddUser() {
        user.setEmail("example@yandex.ru");
        user.setLogin("Chosya");
        user.setName("Chosik");
        user.setBirthday(LocalDate.of(2020, 7, 1));

        userController.userCreate(user);
        assertEquals(user.getId(), 1, "Пользователь не добавлен");
    }

    @Test
    @DisplayName("Проверка на пустой id у пользователя")
    void shouldIUserIsEmpty() {
        user.setId(null);
        user.setEmail("example@yandex.ru");
        user.setLogin("Chosya");
        user.setName("Chosik");
        user.setBirthday(LocalDate.of(2020, 7, 1));

        assertThrows(NotFoundException.class, () -> {
            userController.userUpdate(user);
        });
    }

    @Test
    @DisplayName("Проверка на обновление пользователя")
    void shouldUpdateUser() {
        user.setEmail("example@yandex.ru");
        user.setLogin("Chosya");
        user.setName("Chosik");
        user.setBirthday(LocalDate.of(2020, 7, 1));

        userController.userCreate(user);

        User user1 = new User();
        user1.setEmail("Newexample@yandex.ru");
        user1.setLogin("Chycha");
        user1.setName("Chychhela");
        user1.setBirthday(LocalDate.of(2020, 1, 7));
        user1.setId(1L);

        userController.userUpdate(user1);
        assertEquals(user1.getEmail(), "Newexample@yandex.ru", "Пользователь не обновлен");
        assertEquals(userController.getAllUsers().size(), 1, "Количество пользователей не совпадает");
    }

    @Test
    @DisplayName("Проверка получение списка пользователей")
    void shouldGetAllUsers() {
        user.setEmail("example@yandex.ru");
        user.setLogin("Chosya");
        user.setName("Chosik");
        user.setBirthday(LocalDate.of(2020, 7, 1));

        userController.userCreate(user);

        User user1 = new User();
        user1.setEmail("Newexample@yandex.ru");
        user1.setLogin("Chycha");
        user1.setName("Chychhela");
        user1.setBirthday(LocalDate.of(2020, 1, 7));

        userController.userCreate(user1);

        User user2 = new User();
        user2.setEmail("2Newexample@yandex2.ru");
        user2.setLogin("Bocha");
        user2.setName("Bochka");
        user2.setBirthday(LocalDate.of(2015, 4, 10));

        userController.userCreate(user2);

        assertEquals(userController.getAllUsers().size(), 3, "Количество пользователей не совпадает");
    }

    @Test
    @DisplayName("Проверка на валидацию пользоваетеля, с пустым имейлом")
    void shouldValidationUserForEmptyEmail() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\","
                                + "\"login\":\"Chosya\","
                                + "\"name\":\"Chosik\",\"birthday\":\"2020-07-1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка на валидацию пользоваетеля, с имейлом без символа @")
    void shouldValidationUserWithEmailWithoutSymbol() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"exampleyandex.ru\","
                                + "\"login\":\"Chosya\","
                                + "\"name\":\"Chosik\",\"birthday\":\"2020-07-1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка на валидацию пользователя с пустым логином")
    void shouldValidationUserForEmptyLogin() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"example@yandex.ru\","
                                + "\"login\":\"\","
                                + "\"name\":\"Chosik\",\"birthday\":\"2020-07-1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка на валидацию пользователя с логином, состоящим из пробелов")
    void shouldValidationUserForLoginConsistingOfSpaces() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"example@yandex.ru\","
                                + "\"login\":\"       \","
                                + "\"name\":\"Chosik\",\"birthday\":\"2020-07-1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка на валидацию имени пользователя")
    void shouldValidationForUserName() {
        user.setEmail("example@yandex.ru");
        user.setLogin("Chosya");
        user.setName("");
        user.setBirthday(LocalDate.of(2020, 7, 1));

        userController.userCreate(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    @DisplayName("Проверка на валидацию дня рождения пользователя")
    void shouldValidationTheBirthdayUser() throws Exception {
        user.setEmail("example@yandex.ru");
        user.setLogin("Chosya");
        user.setName("Chosik");
        user.setBirthday(LocalDate.of(2050, 7, 1));

        assertThrows(ValidationException.class, () -> {
            userController.userCreate(user);
        });
    }
}
