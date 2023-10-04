package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DisplayName("Item request repository")
class RequestRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    User owner;
    User requestor;
    ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        owner = userRepository.save(new User(1L, "Rebecca", "becky@mail.com"));
        requestor = userRepository.save(new User(2L, "Selena", "selena@mail.com"));
        itemRequest = itemRequestRepository
                .save(new ItemRequest(1L, "text", requestor, LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should find all requests by requestor ID")
    void shouldFindAllByRequestorId() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestor.getId());
        ItemRequest newRequest = itemRequests.get(0);

        assertNotNull(newRequest, "Request is null");
        assertEquals(1, itemRequests.size(),
                "Incorrect search by text: expected 1, actual - " + itemRequests.size());
        assertEquals(itemRequest.getId(), newRequest.getId(),
                "Incorrect ID: expected " + itemRequest.getId() + ", actual - " + newRequest.getId());
        assertEquals(itemRequest.getId(), newRequest.getId(),
                "Incorrect Description: expected " + itemRequest.getDescription() + ", actual - "
                        + newRequest.getDescription());
        assertEquals(itemRequest.getRequestor().getId(), newRequest.getRequestor().getId(),
                "Incorrect ID: expected " + itemRequest.getRequestor().getId() + ", actual - "
                        + newRequest.getRequestor().getId());
    }

    @Test
    @DisplayName("Should find all requests by user ID")
    void shouldFindAllByUserId() {
        List<ItemRequest> itemRequests =
                itemRequestRepository.findAllByUserId(owner.getId(), PageRequest.ofSize(10));
        ItemRequest newRequest = itemRequests.get(0);

        assertNotNull(newRequest, "Request is null");
        assertEquals(1, itemRequests.size(),
                "Incorrect search by text: expected 1, actual - " + itemRequests.size());
        assertEquals(itemRequest.getId(), newRequest.getId(),
                "Incorrect ID: expected " + itemRequest.getId() + ", actual - " + newRequest.getId());
        assertEquals(itemRequest.getId(), newRequest.getId(),
                "Incorrect Description: expected " + itemRequest.getDescription() + ", actual - "
                        + newRequest.getDescription());
        assertEquals(itemRequest.getRequestor().getId(), newRequest.getRequestor().getId(),
                "Incorrect ID: expected " + itemRequest.getRequestor().getId() + ", actual - "
                        + newRequest.getRequestor().getId());
    }
}