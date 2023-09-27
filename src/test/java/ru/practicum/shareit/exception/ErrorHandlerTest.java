package ru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Error controller")
@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class ErrorHandlerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;

    @Test
    @DisplayName("RETURN -> HttpStatus.NOT_FOUND")
    void testHandlerNotFoundException() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Not found booking with id: " + 1000));

        mvc.perform(get("/bookings/1000").header("X-Sharer-User-Id", 1000L))
                .andExpect(status().isNotFound());

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getById(1000L, 1000L));

        assertEquals("Not found booking with id: " + 1000, exception.getMessage());
    }

    @Test
    @DisplayName("RETURN -> HttpStatus.BAD_REQUEST")
    void testHandlerStatusTypeException() throws Exception {
        when(bookingService.getBookingsByBookerIdAndState(anyLong(), anyString(), any()))
                .thenThrow(new StatusTypeException("ERROR STATE"));

        mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(new BookingRequestDto(0L, LocalDateTime.now(),
                                LocalDateTime.now().minusDays(1))))
                        .header("X-Sharer-User-Id", 1000L))
                .andExpect(status().isBadRequest());

        final StatusTypeException exception = assertThrows(StatusTypeException.class,
                () -> bookingService.getBookingsByBookerIdAndState(1000L, "Error",
                        Pageable.ofSize(100)));

        assertEquals("ERROR STATE", exception.getMessage());
    }
}