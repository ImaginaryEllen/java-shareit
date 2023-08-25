package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(Long itemId, Long userId, ItemDto itemDto);

    ItemDto getById(Long itemId);

    List<ItemDto> getAllItems(Long userId);

    List<ItemDto> search(String text);
}
