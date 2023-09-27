package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User mapper")
class UserMapperTest {
    User user;
    UserDto userDto;

    @BeforeEach
    void beforeEach() {
        user = new User(10L, "Kenny", "ken@mail.com");
        userDto = new UserDto(20L, "Polina", "polly@mail.ru");
    }

    @Test
    @DisplayName("should mapping user -> userDto")
    void shouldToUserDto() {
        UserDto dto = UserMapper.toUserDto(user);

        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(user.getId()));
        assertThat(dto.getName(), equalTo(user.getName()));
        assertThat(dto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("should mapping userDto -> user")
    void shouldToUser() {
        User newUser = UserMapper.toUser(userDto);

        assertNotNull(newUser, "New user is null");
        assertThat(newUser.getId(), equalTo(userDto.getId()));
        assertThat(newUser.getName(), equalTo(userDto.getName()));
        assertThat(newUser.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    @DisplayName("should mapping user list -> userDto list")
    void shouldToUserDtoList() {
        List<User> users = List.of(user);
        List<UserDto> userDtoList = UserMapper.toUserDtoList(users);
        UserDto dto = userDtoList.get(0);

        assertEquals(users.size(), userDtoList.size(),
                "Incorrect size: expected " + users.size() + ", actual - " + userDtoList.size());
        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(user.getId()));
        assertThat(dto.getName(), equalTo(user.getName()));
        assertThat(dto.getEmail(), equalTo(user.getEmail()));
    }
}