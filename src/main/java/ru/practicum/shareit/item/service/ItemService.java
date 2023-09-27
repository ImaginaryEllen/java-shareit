package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemInfoDto;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(Long itemId, Long userId, ItemDto itemDto);

    ItemInfoDto getById(Long userId, Long itemId);

    List<ItemInfoDto> getAllItems(Long userId, Pageable pageable);

    List<ItemDto> search(String text, Pageable pageable);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

}
