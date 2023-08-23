package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Validated(Create.class)
    public UserDto create(@RequestBody @Valid UserDto user) {
        log.info("Create user: {} - STARTED", user);
        UserDto userDto = userService.create(user);
        log.info("Create user: {} - FINISHED", userDto);
        return userDto;
    }

    @PatchMapping("/{userId}")
    @Validated(Update.class)
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto user) {
        log.info("Update user: {} - STARTED", user);
        UserDto userDto = userService.update(userId, user);
        log.info("Update user: {} - FINISHED", userDto);
        return userDto;
    }

    @GetMapping
    public List<UserDto> get() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.info("Get user by id: {}", userId);
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Delete user by id: {} - STARTED", userId);
        userService.delete(userId);
        log.info("Delete user by id: {} - FINISHED", userId);
    }
}