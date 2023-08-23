package ru.practicum.shareit.item.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long lastId = 0L;

    @Override
    public Item create(Item item) {
        item.setId(++lastId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long itemId, Item item) {
        Item updateItem = items.get(itemId);
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        items.put(itemId, updateItem);
        return updateItem;
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner()
                        .getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        String form = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(form)
                        || item.getDescription().toLowerCase().contains(form)
                        && item.getAvailable()).collect(Collectors.toList());
    }
}
