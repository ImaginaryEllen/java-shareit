package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Booking repository")
class BookingRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;
    User owner;
    User booker;
    Item item;
    Booking booking;
    LocalDateTime start;
    LocalDateTime end;

    @BeforeEach
    void beforeEach() {
        start = LocalDateTime.now().plusDays(2);
        end = LocalDateTime.now().plusDays(3);
        owner = userRepository.save(new User(1L, "Emma", "em@mail.com"));
        booker = userRepository.save(new User(2L, "Nick", "nick@mail.com"));
        item = itemRepository.save(new Item(1L, "Camp", "double camp", true, owner,
                new ArrayList<>(), null));
        booking = bookingRepository.save(new Booking(1L, start, end, item, booker, StatusType.WAITING));
    }

    @Test
    @DisplayName("Should find by booking ID")
    void shouldFindBookingById() {
        Booking newBooking = bookingRepository.findById(booking.getId()).orElse(null);

        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }

    @Test
    @DisplayName("Should find bookings by booker ID")
    void shouldFindAllByBookerId() {
        Pageable pageable = PageRequest.ofSize(10);
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndOrderByStartTimeDesc(booker.getId(), pageable);
        Booking newBooking = bookings.get(0);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(1, bookings.size(), "Incorrect booking list size");
        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }

    @Test
    @DisplayName("Should find CURRENT bookings by booker ID")
    void shouldFindAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter() {
        Pageable pageable = PageRequest.ofSize(10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeAsc(
                        booker.getId(), now, now, pageable);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(0, bookings.size(), "Incorrect booking list size");
    }

    @Test
    @DisplayName("Should find PAST bookings by booker ID")
    void shouldFindAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsBefore() {
        Pageable pageable = PageRequest.ofSize(10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
                        booker.getId(), now, now, pageable);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(0, bookings.size(), "Incorrect booking list size");
    }

    @Test
    @DisplayName("Should find FUTURE bookings by booker ID")
    void shouldFindAllByBookerIdAndStartTimeIsAfterAndEndTimeIsAfter() {
        Pageable pageable = PageRequest.ofSize(10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
                        booker.getId(), now, now, pageable);
        Booking newBooking = bookings.get(0);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(1, bookings.size(), "Incorrect booking list size");
        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }

    @Test
    @DisplayName("Should find WAITING bookings by booker ID")
    void shouldFindAllByBookerIdAndStatus() {
        Pageable pageable = PageRequest.ofSize(10);
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartTimeDesc(
                        booker.getId(), StatusType.WAITING, pageable);
        Booking newBooking = bookings.get(0);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(1, bookings.size(), "Incorrect booking list size");
        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }

    @Test
    @DisplayName("Should find bookings by owner ID")
    void shouldFindAllByItemOwnerId() {
        Pageable pageable = PageRequest.ofSize(10);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartTimeDesc(owner.getId(), pageable);
        Booking newBooking = bookings.get(0);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(1, bookings.size(), "Incorrect booking list size");
        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }

    @Test
    @DisplayName("Should get CURRENT booking by owner ID")
    void shouldFindAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter() {
        Pageable pageable = PageRequest.ofSize(10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeDesc(
                        owner.getId(), now, now, pageable);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(0, bookings.size(), "Incorrect booking list size");
    }

    @Test
    @DisplayName("Should get PAST booking by owner ID")
    void shouldFindAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsBefore() {
        Pageable pageable = PageRequest.ofSize(10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
                        owner.getId(), now, now, pageable);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(0, bookings.size(), "Incorrect booking list size");
    }

    @Test
    @DisplayName("Should get FUTURE booking by owner ID")
    void shouldFindAllByItemOwnerIdAndStartTimeIsAfterAndEndTimeIsAfter() {
        Pageable pageable = PageRequest.ofSize(10);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
                        owner.getId(), now, now, pageable);
        Booking newBooking = bookings.get(0);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(1, bookings.size(), "Incorrect booking list size");
        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }

    @Test
    @DisplayName("Should get REJECTED booking by owner ID")
    void shouldFindAllByItemOwnerIdAndStatus() {
        Pageable pageable = PageRequest.ofSize(10);
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndStatusOrderByStartTimeDesc(
                        owner.getId(), StatusType.REJECTED, pageable);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(0, bookings.size(), "Incorrect booking list size");
    }

    @Test
    @DisplayName("Should get last booking by item ID")
    void shouldFindLastBookingByItemId() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> lastBooking = bookingRepository.findLastBookingByItemId(item.getId(), owner.getId(), now);

        assertNotNull(lastBooking, "Bookings list is null");
        assertEquals(0, lastBooking.size(), "Incorrect booking list size");
    }

    @Test
    @DisplayName("Should get next booking by item ID")
    void shouldFindNextBookingByItemId() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> nextBooking = bookingRepository.findNextBookingByItemId(item.getId(), owner.getId(), now);
        Booking newBooking = nextBooking.get(0);

        assertNotNull(nextBooking, "Bookings list is null");
        assertEquals(1, nextBooking.size(), "Incorrect booking list size");
        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }

    @Test
    @DisplayName("Should get bookings by item ID and booker ID")
    void shouldFindBookingsByItemIdAndBookerId() {
        List<Booking> bookings = bookingRepository.findBookingsByItemIdAndBookerId(item.getId(), booker.getId());
        Booking newBooking = bookings.get(0);

        assertNotNull(bookings, "Bookings list is null");
        assertEquals(1, bookings.size(), "Incorrect booking list size");
        assertNotNull(newBooking, "Booking is null");
        assertEquals(booking.getId(), newBooking.getId(),
                "Incorrect ID: expected " + booking.getId() + ", actual - " + newBooking.getId());
        assertEquals(booking.getStartTime(), newBooking.getStartTime(),
                "Incorrect start: expected " + booking.getStartTime() + ", actual - " + newBooking.getStartTime());
        assertEquals(booking.getEndTime(), newBooking.getEndTime(),
                "Incorrect ID: expected " + booking.getEndTime() + ", actual - " + newBooking.getEndTime());
    }
}