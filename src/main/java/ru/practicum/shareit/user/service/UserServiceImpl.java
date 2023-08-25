package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        return userMapper.toUserDto(userRepository.create(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));
        User user = userRepository.update(userId, userMapper.toUser(userDto));
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long userId) {
        return userMapper.toUserDto(userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId)));
    }

    @Override
    public void delete(Long userId) {
        userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));
        userRepository.delete(userId);
    }
}
