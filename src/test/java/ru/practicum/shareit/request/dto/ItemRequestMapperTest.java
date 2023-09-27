package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Item request mapper")
class ItemRequestMapperTest {
    User requestor;
    ItemRequestInDto inDto;
    ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        requestor = new User(10L, "Alex", "alexxx@mail.com");
        inDto = new ItemRequestInDto(5L, "canon", requestor.getId(), LocalDateTime.now());
        itemRequest = new ItemRequest(7L, "camera", requestor, LocalDateTime.now());
    }

    @Test
    @DisplayName("should mapping item request in dto -> item request")
    void shouldToItemRequest() {
        ItemRequest request = ItemRequestMapper.toItemRequest(requestor, inDto);

        assertNotNull(request, "Request is null");
        assertThat(inDto.getId(), equalTo(request.getId()));
        assertThat(inDto.getDescription(), equalTo(request.getDescription()));
        assertThat(inDto.getRequestorId(), equalTo(request.getRequestor().getId()));
        assertThat(inDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE),
                equalTo(request.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    @DisplayName("should mapping item request-> item request out dto")
    void shouldToItemRequestOutDto() {
        ItemRequestOutDto outDto = ItemRequestMapper.toItemRequestOutDto(itemRequest, Collections.emptyList());

        assertNotNull(outDto, "Request is null");
        assertThat(itemRequest.getId(), equalTo(outDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(outDto.getDescription()));
        assertThat(itemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                equalTo(outDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }
}