package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Booking service integration")
class BookingServiceIntegrationTest {
    private final EntityManager em;
    private final BookingServiceImpl bookingService;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Test
    @Order(value = 1)
    @DisplayName("Should create booking from repository")
    void shouldCreateBooking() {
        UserDto owner = userService.create(new UserDto(0L, "Carl", "carl@mail.com"));
        UserDto booker = userService.create(new UserDto(0L, "Jacob", "jacobo@mail.com"));
        ItemDto itemDto = itemService.create(new ItemDto(0L, "Map", "world map",
                true, null), owner.getId());
        BookingResponseDto responseDto = bookingService.create(booker.getId(),
                new BookingRequestDto(itemDto.getId(), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4)));

        TypedQuery<Booking> query = em.createQuery("select b from Booking as b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", responseDto.getId()).getSingleResult();

        assertNotNull(responseDto, "Return booking response dto is null");
        assertNotNull(booking, "Return booking is null");
        assertThat(booking.getStartTime(), equalTo(responseDto.getStart()));
        assertThat(booking.getEndTime(), equalTo(responseDto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(responseDto.getItem().getId()));
    }

    @Order(value = 2)
    @DisplayName("Should find bookings by booker ID from repository")
    @Test
    void shouldGetBookingsByBookerIdAndState() {
        UserDto owner = userService.create(new UserDto(0L, "Carl", "carl@mail.com"));
        UserDto booker = userService.create(new UserDto(0L, "Jacob", "jacobo@mail.com"));
        ItemDto itemDto = itemService.create(new ItemDto(0L, "Map", "world map",
                true, null), owner.getId());
        bookingService.create(booker.getId(),
                new BookingRequestDto(itemDto.getId(), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4)));

        List<BookingResponseDto> dtoList = bookingService.getBookingsByBookerIdAndState(booker.getId(), "WAITING",
                Pageable.ofSize(10));
        BookingResponseDto dto = dtoList.get(0);

        TypedQuery<Booking> query = em.createQuery(
                "select b from Booking as b where b.booker.id = :id", Booking.class);
        query.setParameter("id", booker.getId());

        List<Booking> bookings = query.getResultList();
        Booking booking = bookings.get(0);

        assertNotNull(dto, "Return booking response dto is null");
        assertNotNull(booking, "Return booking is null");
        assertThat(booking.getStartTime(), equalTo(dto.getStart()));
        assertThat(booking.getEndTime(), equalTo(dto.getEnd()));
        assertThat(booking.getItem().getId(), equalTo(dto.getItem().getId()));
    }
}