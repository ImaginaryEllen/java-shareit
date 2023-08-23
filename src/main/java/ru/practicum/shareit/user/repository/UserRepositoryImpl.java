package ru.practicum.shareit.user.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 0L;

    @Override
    public User create(User user) {
        user.setId(++lastId);
        if (!isDuplicate(user.getId(), user.getEmail())) {
            --lastId;
            throw new DuplicateException("Email: " + user.getEmail() + " already exist, please choose another email");
        }
        Boolean l = true;
        System.out.println(l);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long userId, User updateUser) {
        String email = updateUser.getEmail();
        String name = updateUser.getName();
        User user = users.get(userId);
        if (name != null) {
            user.setName(name);
        }
        if (email != null && !isDuplicate(userId, email)) {
            throw new DuplicateException("Email: " + email + " already exist, please choose another email");
        } else if (email != null) {
            user.setEmail(email);
        }
        users.put(userId, user);
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

    private Boolean isDuplicate(Long userId, String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email) && !user.getId().equals(userId)) {
                return false;
            }
        }
        return true;
    }
}