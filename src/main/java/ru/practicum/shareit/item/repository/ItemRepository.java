package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item create(Item item);

    Item update(Long itemId, Item item);

    Optional<Item> getById(Long itemId);

    List<Item> getAllItems(Long userId);

    List<Item> search(String text);
}
