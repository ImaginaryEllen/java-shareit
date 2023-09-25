package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Item repository")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    User owner;
    User requestor;
    ItemRequest itemRequest;
    Item item;

    @BeforeEach
    void beforeEach() {
        owner = userRepository.save(new User(1L, "Jose", "jo@mail.com"));
        requestor = userRepository.save(new User(2L, "Kate", "kkk@mail.com"));
        itemRequest = itemRequestRepository
                .save(new ItemRequest(1L, "text", requestor, LocalDateTime.now()));
        item = itemRepository.save(new Item(1L, "Harry Potter 1", "book", true, owner,
                new ArrayList<>(), itemRequest));
    }

    @Test
    @DisplayName("Should find by owner ID")
    void shouldFindByOwnerIdOrderById() {
        Pageable pageable = PageRequest.ofSize(10);
        List<Item> items = itemRepository.findByOwnerIdOrderById(owner.getId(), pageable);
        Item newItem = items.get(0);

        assertNotNull(newItem, "Item is null");
        assertEquals(1, items.size(), "Incorrect get by owner id: expected 1, actual - " + items.size());
        assertEquals(item.getId(), newItem.getId(),
                "Incorrect ID: expected " + item.getId() + ", actual - " + newItem.getId());
    }

    @Test
    @DisplayName("Should search items by text")
    void shouldSearchItems() {
        String text = "book";
        Pageable pageable = PageRequest.ofSize(10);
        List<Item> items = itemRepository.search(text, pageable);
        Item newItem = items.get(0);

        assertNotNull(newItem, "Item is null");
        assertEquals(1, items.size(), "Incorrect search by text: expected 1, actual - " + items.size());
        assertEquals(item.getId(), newItem.getId(),
                "Incorrect ID: expected " + item.getId() + ", actual - " + newItem.getId());
    }

    @Test
    @DisplayName("Should find items by request id")
    void findAllByItemRequestId() {
        List<Item> items = itemRepository.findAllByItemRequestId(itemRequest.getId());
        Item newItem = items.get(0);

        assertNotNull(newItem, "Item is null");
        assertEquals(1, items.size(),
                "Incorrect get by request id: expected 1, actual - " + items.size());
        assertEquals(item.getId(), newItem.getId(),
                "Incorrect ID: expected " + item.getId() + ", actual - " + newItem.getId());
    }
}