package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Item request service")
class RequestServiceImplTest {
    ItemRequestServiceImpl requestService;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    User owner;
    User requestor;
    ItemRequest itemRequest;
    ItemRequestInDto inDto;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        requestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRepository);
        owner = new User(1L, "Nataly", "nata@mail.com");
        requestor = new User(2L, "Scott", "scott@mail.com");
        inDto = new ItemRequestInDto(1L, "photo lamp", requestor.getId(), LocalDateTime.now());
        itemRequest = ItemRequestMapper.toItemRequest(requestor, inDto);
    }

    @Test
    @DisplayName("Should create item request")
    void shouldCreateItemRequest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestOutDto outDto = requestService.create(requestor.getId(), inDto);

        assertNotNull(outDto, "Return dto is null");
        assertThat(outDto.getId(), equalTo(inDto.getId()));
        assertThat(outDto.getDescription(), equalTo(inDto.getDescription()));
        assertThat(outDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE),
                equalTo(inDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    @DisplayName("Should get item request by ID")
    void shouldGetItemRequestById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestOutDto outDto = requestService.getById(requestor.getId(), inDto.getId());

        assertNotNull(outDto, "Return dto is null");
        assertThat(outDto.getId(), equalTo(inDto.getId()));
        assertThat(outDto.getDescription(), equalTo(inDto.getDescription()));
        assertThat(outDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE),
                equalTo(inDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    @DisplayName("Should get item requests by ID")
    void shouldGetItemRequestsById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(anyLong()))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestOutDto> outDtoList = requestService.getAllById(requestor.getId());
        ItemRequestOutDto outDto = outDtoList.get(0);

        assertNotNull(outDtoList, "Return list is null");
        assertNotNull(outDto, "Return dto is null");
        assertThat(outDto.getId(), equalTo(inDto.getId()));
        assertThat(outDto.getDescription(), equalTo(inDto.getDescription()));
        assertThat(outDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE),
                equalTo(inDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    @DisplayName("Should get all item requests")
    void shouldGetAll() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRequestRepository.findAllByUserId(anyLong(), any()))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestOutDto> outDtoList = requestService.getAll(owner.getId(), PageRequest.ofSize(10));
        ItemRequestOutDto outDto = outDtoList.get(0);

        assertNotNull(outDtoList, "Return list is null");
        assertNotNull(outDto, "Return dto is null");
        assertThat(outDto.getId(), equalTo(inDto.getId()));
        assertThat(outDto.getDescription(), equalTo(inDto.getDescription()));
        assertThat(outDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE),
                equalTo(inDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }
}