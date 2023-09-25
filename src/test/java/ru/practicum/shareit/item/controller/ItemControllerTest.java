package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Item controller")
@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private ItemDto itemDto;
    private ItemInfoDto itemInfoDto;
    private User user;
    private CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto(1L, "USB", "USB flash drive 32GB", true, null);
        itemInfoDto = new ItemInfoDto(1L, "USB", "USB flash drive 32GB", true, null,
                null, new ArrayList<>());
        user = new User(1L, "Tom", "tommy@mail.com");
        commentDto = new CommentDto(1L, "Good USB", user.getName(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create item")
    void shouldCreateItem() throws Exception {
        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    @DisplayName("Should update item")
    void shouldUpdateItem() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
    }

    @Test
    @DisplayName("Should get all items")
    void shouldGetAllItems() throws Exception {
        int from = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        List<ItemInfoDto> all = Collections.emptyList();
        when(itemService.getAllItems(user.getId(), pageable))
                .thenReturn(all);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(all)));
        verify(itemService, times(1))
                .getAllItems(user.getId(), pageable);
    }

    @Test
    @DisplayName("Should get item by ID")
    void shouldGetItemById() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemInfoDto);

        mvc.perform(get("/items/1").header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemInfoDto)));
        verify(itemService, times(1)).getById(user.getId(), 1L);
    }

    @Test
    @DisplayName("Should search item by text")
    void shouldSearchItem() throws Exception {
        int from = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        String text = "";
        List<ItemDto> all = Collections.emptyList();
        when(itemService.search(text, pageable))
                .thenReturn(all);

        mvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(all)));
        verify(itemService, times(1))
                .search(text, pageable);
    }

    @Test
    @DisplayName("Should add new comment")
    void shouldAddComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated()
                        .format(DateTimeFormatter.ISO_DATE_TIME)), LocalDateTime.class));
    }
}