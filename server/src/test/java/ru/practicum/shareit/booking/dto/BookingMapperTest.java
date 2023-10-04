package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Booking mapper")
class BookingMapperTest {
    User booker;
    Item item;
    Booking booking;
    BookingDto bookingDto;
    BookingRequestDto requestDto;

    @BeforeEach
    void beforeEach() {
        booker = new User(10L, "Lolita", "lol@mail.com");
        item = new Item(10L, "UNO", "card game", true, booker,
                new ArrayList<>(), null);
        booking = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10),
                item, booker, StatusType.WAITING);
        bookingDto = new BookingDto(
                11L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10), booker.getId());
        requestDto = new BookingRequestDto(
                item.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10));
    }

    @Test
    @DisplayName("should mapping booking -> bookingDto")
    void shouldToBookingDto() {
        BookingDto dto = BookingMapper.toBookingDto(booking);

        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(booking.getId()));
        assertThat(dto.getStart(), equalTo(booking.getStartTime()));
        assertThat(dto.getEnd(), equalTo(booking.getEndTime()));
        assertThat(dto.getBookerId(), equalTo(booking.getBooker().getId()));
    }

    @Test
    @DisplayName("should mapping bookingDto -> booking")
    void shouldToBooking() {
        Booking newBooking = BookingMapper.toBooking(requestDto, booker, item);

        assertNotNull(newBooking, "Dto is null");
        assertThat(newBooking.getItem().getId(), equalTo(requestDto.getItemId()));
        assertThat(newBooking.getStartTime(), equalTo(requestDto.getStart()));
        assertThat(newBooking.getEndTime(), equalTo(requestDto.getEnd()));
    }

    @Test
    @DisplayName("should mapping booking -> booking response dto")
    void shouldToBookingResponseDto() {
        BookingResponseDto dto = BookingMapper.toBookingResponseDto(booking);

        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(booking.getId()));
        assertThat(dto.getStart(), equalTo(booking.getStartTime()));
        assertThat(dto.getEnd(), equalTo(booking.getEndTime()));
    }

    @Test
    @DisplayName("should mapping booking list -> bookingDto list")
    void shouldToBookingResponseDtoList() {
        List<BookingResponseDto> responseDtoList = BookingMapper.toBookingResponseDtoList(List.of(booking));
        BookingResponseDto dto = responseDtoList.get(0);

        assertNotNull(responseDtoList, "List booking response dto is null");
        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(booking.getId()));
        assertThat(dto.getStart(), equalTo(booking.getStartTime()));
        assertThat(dto.getEnd(), equalTo(booking.getEndTime()));
    }
}