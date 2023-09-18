package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.constant.Constant.USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestHeader(USER_ID) Long userId,
                                     @Validated @RequestBody BookingRequestDto booking) {
        log.info("Create booking: {} - STARTED", booking);
        BookingResponseDto responseDto = bookingService.create(userId, booking);
        log.info("Create booking: {} - FINISHED", responseDto);
        return responseDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader(USER_ID) Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam(value = "approved") boolean isApproved) {
        log.info("Update booking status: {} - STARTED", isApproved);
        BookingResponseDto responseDto = bookingService.update(userId, bookingId, isApproved);
        log.info("Update booking: {} - FINISHED", responseDto);
        return responseDto;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(USER_ID) Long userId, @PathVariable Long bookingId) {
        log.info("Getting booking by id: {}", bookingId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByUser(@RequestHeader(USER_ID) Long userId,
                                                      @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Getting bookings list by booker id: {} with state : {}", userId, state);
        return bookingService.getBookingsByBookerIdAndStatus(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwner(@RequestHeader(USER_ID) Long userId,
                                                       @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Getting bookings list by owner id: {} with state : {}", userId, state);
        return bookingService.getBookingsByOwnerAndState(userId, state);
    }
}
