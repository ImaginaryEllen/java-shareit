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
    private final Map<Long, List<Item>> userItems = new LinkedHashMap<>();
    private Long lastId = 0L;

    @Override
    public Item create(Item item) {
        item.setId(++lastId);
        items.put(item.getId(), item);
        final List<Item> items = userItems.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        items.add(item);
        userItems.put(item.getOwner().getId(), items);
        return item;
    }

    @Override
    public Item update(Long itemId, Item item) {
        Item updateItem = items.get(itemId);
        if (item.getName() != null && !item.getName().isBlank()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return updateItem;
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return userItems.get(userId);
    }

    @Override
    public List<Item> search(String text) {
        String form = text.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(form))
                        || (item.getDescription().toLowerCase().contains(form))
                        && (item.getAvailable())).collect(Collectors.toList());
    }
}