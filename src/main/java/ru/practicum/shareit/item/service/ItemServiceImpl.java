package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));
        Item item = itemRepository.create(itemMapper.toItem(itemDto, owner));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        Item checkItem = itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException("Not found item with id: " + itemId));
        User owner = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));
        if (!userId.equals(checkItem.getOwner().getId())) {
            throw new NotFoundException("User with id: " + userId + " have not item with id: " + itemId);
        }
        Item item = itemRepository.update(itemId, itemMapper.toItem(itemDto, owner));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemMapper.toItemDto(itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException("Not found item with id: " + itemId)));
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return itemRepository.getAllItems(userId).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
