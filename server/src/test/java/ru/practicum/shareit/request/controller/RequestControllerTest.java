package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Request controller")
@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class RequestControllerTest {
    @MockBean
    ItemRequestService requestService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private User requestor;
    private ItemRequestInDto inDto;
    private ItemRequestOutDto outDto;

    @BeforeEach
    void beforeEach() {
        requestor = new User(1L, "Viola", "viavio@mail.com");
        inDto = new ItemRequestInDto(1L, "guitar", requestor.getId(), LocalDateTime.now());
        ItemDto itemDto = new ItemDto(1L, "Guitar", "acoustic", true, null);
        outDto = new ItemRequestOutDto(1L, "guitar", LocalDateTime.now(), List.of(itemDto));
    }

    @Test
    @DisplayName("Should create item request")
    void shouldCreateItemRequest() throws Exception {
        when(requestService.create(anyLong(), any()))
                .thenReturn(outDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(inDto))
                        .header("X-Sharer-User-Id", requestor.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(outDto.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(outDto.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), LocalDateTime.class));
    }

    @Test
    @DisplayName("Should get item request by ID")
    void shouldGetItemRequestById() throws Exception {
        when(requestService.getById(anyLong(), anyLong()))
                .thenReturn(outDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", requestor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outDto)));
        verify(requestService, times(1)).getById(requestor.getId(), inDto.getId());
    }

    @Test
    @DisplayName("Should get all item requests by user ID")
    void shouldGetAllItemRequestsById() throws Exception {
        List<ItemRequestOutDto> all = Collections.singletonList(outDto);
        when(requestService.getAllById(anyLong()))
                .thenReturn(all);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", requestor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(all)));
        verify(requestService, times(1)).getAllById(requestor.getId());
    }

    @Test
    @DisplayName("Should get all item requests")
    void shouldGetAllItemRequests() throws Exception {
        int from = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        List<ItemRequestOutDto> all = Collections.singletonList(outDto);
        when(requestService.getAll(anyLong(), any()))
                .thenReturn(all);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", requestor.getId())
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(all)));
        verify(requestService, times(1)).getAll(requestor.getId(), pageable);
    }
}