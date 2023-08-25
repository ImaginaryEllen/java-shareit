package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 0L;

    @Override
    public User create(User user) {
        user.setId(++lastId);
        checkDuplicate(user.getId(), user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long userId, User updateUser) {
        String email = updateUser.getEmail();
        String name = updateUser.getName();
        User user = users.get(userId);
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        checkDuplicate(userId, email);
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    private void checkDuplicate(Long userId, String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email) && !Objects.equals(user.getId(), userId)) {
                lastId = users.keySet().stream().max(Long::compareTo).get();
                throw new DuplicateException("Email: " + email + " already exist, please choose another email");
            }
        }
    }
}