package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getBooker().getId()
        );
    }

    public static Booking toBooking(BookingRequestDto requestDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setStartTime(requestDto.getStart());
        booking.setEndTime(requestDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(StatusType.WAITING);
        return booking;
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getBooker(),
                booking.getItem()
        );
    }

    public static List<BookingResponseDto> toBookingResponseDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toBookingResponseDto).collect(Collectors.toList());
    }
}
