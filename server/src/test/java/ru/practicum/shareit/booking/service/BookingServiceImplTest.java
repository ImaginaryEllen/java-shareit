package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Booking service")
class BookingServiceImplTest {
    BookingServiceImpl bookingService;
    BookingRepository bookingRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;
    Item item;
    User owner;
    User booker;
    Booking booking;
    LocalDateTime start;
    LocalDateTime end;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        start = LocalDateTime.now().plusDays(2);
        end = LocalDateTime.now().plusDays(3);
        owner = new User(1L, "Kim", "kimmy@mail.com");
        booker = new User(2L, "Milena", "mimi@mail.com");
        item = new Item(1L, "Mirror", "mirror for make up", true, owner,
                new ArrayList<>(), null);
        booking = new Booking(1L, start, end, item, booker, StatusType.WAITING);
    }

    @Test
    @DisplayName("Should create booking")
    void shouldCreateBooking() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingRequestDto requestDto = new BookingRequestDto(item.getId(), start, end);
        BookingResponseDto responseDto = bookingService.create(booker.getId(), requestDto);

        assertNotNull(requestDto, "Return booking request dto is null");
        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(requestDto.getStart(), equalTo(responseDto.getStart()));
        assertThat(requestDto.getEnd(), equalTo(responseDto.getEnd()));
        assertThat(requestDto.getItemId(), equalTo(responseDto.getItem().getId()));
        assertThat(booker.getId(), equalTo(responseDto.getBooker().getId()));
    }

    @Test
    @DisplayName("Should update booking")
    void shouldUpdateBooking() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingResponseDto responseDto = bookingService.update(owner.getId(), booking.getId(), Boolean.TRUE);

        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
    }

    @Test
    @DisplayName("Should get booking by ID")
    void shouldGetBookingById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));

        BookingResponseDto responseDto = bookingService.getById(booker.getId(), booking.getId());

        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
        assertThat(booker.getId(), equalTo(responseDto.getBooker().getId()));
    }

    @Test
    @DisplayName("Should get ALL booking by booker ID")
    void shouldGetBookingsByBookerIdAndStateALL() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "ALL";
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBookerIdAndOrderByStartTimeDesc(booker.getId(), pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByBookerIdAndState(booker.getId(), state, pageable);
        BookingResponseDto responseDto = dtoList.get(0);

        assertNotNull(dtoList, "Return booking dto list is null");
        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
        assertThat(booker.getId(), equalTo(responseDto.getBooker().getId()));
    }

    @Test
    @DisplayName("Should get ALL booking by owner ID")
    void shouldGetBookingsByOwnerAndStateALL() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "ALL";
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(owner.getId(), pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByOwnerAndState(owner.getId(), state, pageable);
        BookingResponseDto responseDto = dtoList.get(0);

        assertNotNull(dtoList, "Return booking dto list is null");
        assertNotNull(responseDto, "Return response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
    }

    @Test
    @DisplayName("Should get CURRENT booking by booker ID")
    void shouldGetBookingsByBookerIdAndStateCURRENT() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "CURRENT";
        LocalDateTime now = LocalDateTime.now();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeAsc(
                booker.getId(), now, now, pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByBookerIdAndState(booker.getId(), state, pageable);
        assertNotNull(dtoList, "Return booking dto list is null");
    }

    @Test
    @DisplayName("Should get CURRENT booking by owner ID")
    void shouldGetBookingsByOwnerIdAndStateCURRENT() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "CURRENT";
        LocalDateTime now = LocalDateTime.now();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeDesc(
                owner.getId(), now, now, pageable))
                .thenReturn(Collections.emptyList());

        List<BookingResponseDto> dtoList = bookingService.getBookingsByOwnerAndState(owner.getId(), state, pageable);
        assertNotNull(dtoList, "Return booking dto list is null");
        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("Should get PAST booking by booker ID")
    void shouldGetBookingsByBookerIdAndStatePAST() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "PAST";
        LocalDateTime now = LocalDateTime.now();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
                booker.getId(), now, now, pageable))
                .thenReturn(Collections.emptyList());

        List<BookingResponseDto> dtoList = bookingService.getBookingsByBookerIdAndState(booker.getId(), state, pageable);
        assertNotNull(dtoList, "Return booking dto list is null");
        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("Should get PAST booking by owner ID")
    void shouldGetBookingsByOwnerIdAndStatePAST() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "PAST";
        LocalDateTime now = LocalDateTime.now();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
                owner.getId(), now, now, pageable))
                .thenReturn(Collections.emptyList());

        List<BookingResponseDto> dtoList = bookingService.getBookingsByOwnerAndState(owner.getId(), state, pageable);
        assertNotNull(dtoList, "Return booking dto list is null");
        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("Should get FUTURE booking by booker ID")
    void shouldGetBookingsByBookerIdAndStateFUTURE() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "FUTURE";
        LocalDateTime now = LocalDateTime.now();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBookerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
                booker.getId(), now, now, pageable))
                .thenReturn(Collections.emptyList());

        List<BookingResponseDto> dtoList = bookingService.getBookingsByBookerIdAndState(booker.getId(), state, pageable);
        assertNotNull(dtoList, "Return booking dto list is null");
        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("Should get FUTURE booking by owner ID")
    void shouldGetBookingsByOwnerIdAndStateFUTURE() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "FUTURE";
        LocalDateTime now = LocalDateTime.now();
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
                owner.getId(), now, now, pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByOwnerAndState(owner.getId(), state, pageable);
        assertNotNull(dtoList, "Return booking dto list is null");
    }

    @Test
    @DisplayName("Should get WAITING booking by booker ID")
    void shouldGetBookingsByBookerIdAndStateWAITING() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "WAITING";
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartTimeDesc(booker.getId(), StatusType.WAITING, pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByBookerIdAndState(booker.getId(), state, pageable);
        BookingResponseDto responseDto = dtoList.get(0);

        assertNotNull(dtoList, "Return booking dto list is null");
        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
        assertThat(booker.getId(), equalTo(responseDto.getBooker().getId()));
    }

    @Test
    @DisplayName("Should get WAITING booking by owner ID")
    void shouldGetBookingsByOwnerIdAndStateWAITING() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "WAITING";
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartTimeDesc(owner.getId(), StatusType.WAITING, pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByOwnerAndState(owner.getId(), state, pageable);
        BookingResponseDto responseDto = dtoList.get(0);

        assertNotNull(dtoList, "Return booking dto list is null");
        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
    }

    @Test
    @DisplayName("Should get REJECTED booking by booker ID")
    void shouldGetBookingsByBookerIdAndStateREJECTED() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "REJECTED";
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartTimeDesc(booker.getId(), StatusType.REJECTED, pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByBookerIdAndState(booker.getId(), state, pageable);
        BookingResponseDto responseDto = dtoList.get(0);

        assertNotNull(dtoList, "Return booking dto list is null");
        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
        assertThat(booker.getId(), equalTo(responseDto.getBooker().getId()));
    }

    @Test
    @DisplayName("Should get REJECTED booking by owner ID")
    void shouldGetBookingsByOwnerIdAndStateREJECTED() {
        Pageable pageable = PageRequest.ofSize(10);
        String state = "REJECTED";
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartTimeDesc(owner.getId(), StatusType.REJECTED, pageable))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByOwnerAndState(owner.getId(), state, pageable);
        BookingResponseDto responseDto = dtoList.get(0);

        assertNotNull(dtoList, "Return booking dto list is null");
        assertNotNull(responseDto, "Return booking response dto is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
        assertThat(booker.getId(), equalTo(responseDto.getBooker().getId()));
    }
}