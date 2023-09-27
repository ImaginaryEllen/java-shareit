package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("User repository")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    User user1;
    User user2;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(0L, "Bob", "bob@mail.com"));
        user2 = userRepository.save(new User(0L, "Anna", "ann@mail.com"));
    }

    @Test
    @DisplayName("Should delete user by ID")
    void shouldDeleteUserById() {
        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());

        userRepository.deleteUserById(user2.getId());

        List<User> newUsers = userRepository.findAll();
        assertEquals(1, newUsers.size(),
                "Incorrect deleted user by id: expected 1 user in repository, actual - " + newUsers.size());
    }
}