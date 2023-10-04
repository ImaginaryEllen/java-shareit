package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@DisplayName("User service")
class UserServiceImplTest {
    UserServiceImpl userService;
    UserRepository userRepository;
    User user;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        user = new User(1L, "Molly", "molly@mail.com");
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        when(userRepository.save(any()))
                .thenReturn(user);

        UserDto userDto = userService.create(UserMapper.toUserDto(user));
        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        when(userRepository.save(any()))
                .thenReturn(user);
        when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));

        UserDto userDto = userService.update(user.getId(), UserMapper.toUserDto(user));
        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(user));

        List<UserDto> userDtoList = userService.getAllUsers();

        assertNotNull(userDtoList, "UserDto list is null");
        assertEquals(1, userDtoList.size(), "Incorrect size: expected 1, actual - " + userDtoList.size());

        UserDto userDto = userDtoList.get(0);

        assertNotNull(userDto, "UserDto is null");
        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Should get user by ID")
    void shouldGetUserById() {
        when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));

        UserDto userDto = userService.getById(user.getId());

        assertNotNull(userDto, "UserDto is null");
        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Should delete user by ID")
    void shouldDeleteUserById() {
        when(userRepository.save(any()))
                .thenReturn(user);
        when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));
        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        userService.delete(user.getId());

        assertEquals(0, userRepository.findAll().size(), "Incorrect delete user by ID");
    }
}