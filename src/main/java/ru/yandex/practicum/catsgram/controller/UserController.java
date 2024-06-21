package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAllUsers();
    }

    @GetMapping("/user/{id}")
    public User findUserById(@PathVariable("id") long id) {
        return userService.findUserById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }
}
