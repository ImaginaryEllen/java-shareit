package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Booking controller")
@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    User owner;
    User booker;
    Item item;
    BookingDto bookingDto;
    BookingResponseDto responseDto;
    BookingRequestDto requestDto;

    @BeforeEach
    void beforeEach() {
        owner = new User(10L, "John", "john@mail.com");
        booker = new User(11L, "Tess", "tesss@mail.com");
        item = new Item(1L, "War and Peace", "book", true, owner,
                new ArrayList<>(), null);
        bookingDto = new BookingDto(11L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10), booker.getId());
        responseDto = new BookingResponseDto(11L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10),
                StatusType.APPROVED, booker, item);
        requestDto = new BookingRequestDto(item.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10));
    }

    @Test
    @DisplayName("Should create booking")
    void shouldCreateBooking() throws Exception {
        when(bookingService.create(anyLong(), any()))
                .thenReturn(responseDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", booker.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(responseDto.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), LocalDateTime.class))
                .andExpect(jsonPath("$.end", is(responseDto.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), LocalDateTime.class));
    }

    @Test
    @DisplayName("Should update booking")
    void shouldUpdateBooking() throws Exception {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(responseDto);

        mvc.perform(patch("/bookings/11")
                        .param("approved", String.valueOf(Boolean.TRUE))
                        .header("X-Sharer-User-Id", booker.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(responseDto.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), LocalDateTime.class))
                .andExpect(jsonPath("$.end", is(responseDto.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), LocalDateTime.class));
    }

    @Test
    @DisplayName("Should get booking by ID")
    void shouldGetBookingById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(responseDto);

        mvc.perform(get("/bookings/11").header("X-Sharer-User-Id", booker.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)));
        verify(bookingService, times(1)).getById(booker.getId(), responseDto.getId());
    }

    @Test
    @DisplayName("Should get bookings by user ID")
    void shouldGetBookingsByUser() throws Exception {
        int from = 0;
        int size = 5;
        String state = "ALL";
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        List<BookingResponseDto> all = Collections.emptyList();
        when(bookingService.getBookingsByBookerIdAndState(anyLong(), anyString(), any()))
                .thenReturn(all);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(all)));
        verify(bookingService, times(1))
                .getBookingsByBookerIdAndState(booker.getId(), state, pageable);
    }

    @Test
    @DisplayName("Should get bookings by owner ID")
    void shouldGetBookingsByOwner() throws Exception {
        int from = 0;
        int size = 5;
        String state = "ALL";
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        List<BookingResponseDto> all = Collections.emptyList();
        when(bookingService.getBookingsByBookerIdAndState(anyLong(), anyString(), any()))
                .thenReturn(all);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(all)));
        verify(bookingService, times(1))
                .getBookingsByOwnerAndState(owner.getId(), state, pageable);
    }
}