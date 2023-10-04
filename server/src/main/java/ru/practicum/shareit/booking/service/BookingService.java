package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(Long bookerId, BookingRequestDto bookingDto);

    BookingResponseDto update(Long bookerId, Long bookingId, boolean isApproved);

    BookingResponseDto getById(Long bookerId, Long bookingId);

    List<BookingResponseDto> getBookingsByBookerIdAndState(Long bookerId, String state, Pageable pageable);

    List<BookingResponseDto> getBookingsByOwnerAndState(Long ownerId, String state, Pageable pageable);
}
