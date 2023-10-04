package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User service integration")
class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserServiceImpl userService;

    @Test
    @Order(value = 1)
    @DisplayName("should create user from repository")
    void shouldCreateUser() {
        UserDto userDto = userService.create(new UserDto(0L, "Bill", "billy@mail.com"));

        TypedQuery<User> query = em.createQuery("select u from User as u where u.id = :id", User.class);
        User user = query.setParameter("id", userDto.getId()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("should get all users from repository")
    void shouldGetAllUsers() {
        UserDto dto = userService.create(new UserDto(0L, "Lizzy", "lizzz@mail.com"));

        List<UserDto> userDtoList = userService.getAllUsers();
        UserDto userDto = userDtoList.get(0);

        assertThat(dto.getId(), notNullValue());
        assertThat(userDto.getId(), notNullValue());
        assertThat(dto.getId(), equalTo(userDto.getId()));
        assertThat(dto.getName(), equalTo(userDto.getName()));
        assertThat(dto.getEmail(), equalTo(userDto.getEmail()));

        TypedQuery<User> query = em.createQuery("select u from User as u", User.class);
        List<User> users = query.getResultList();
        User user = users.get(0);

        assertThat(users.size(), equalTo(userDtoList.size()));
        assertThat(user.getId(), notNullValue());
        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }
}