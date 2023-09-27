package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.type.StateType;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusTypeException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingResponseDto create(Long bookerId, BookingRequestDto bookingRequestDto) {
        User booker = checkUser(bookerId);
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Not found item with id: " + bookingRequestDto.getItemId()));
        if (bookerId.equals(item.getOwner().getId())) {
            throw new NotFoundException("User with id: " + bookerId + " is owner -> item with id: " + item.getId());
        }
        if (!item.getAvailable()) {
            throw new StatusTypeException("Item with id: " + item.getId() + " cannot booking");
        }
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingRequestDto, booker, item));
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Transactional
    @Override
    public BookingResponseDto update(Long userId, Long bookingId, boolean isApproved) {
        User user = checkUser(userId);
        Booking booking = checkBooking(bookingId);
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Not found item with id: " + booking.getItem().getId()));
        if (!user.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("User with id: " + userId + " have not item with id: " + item.getId());
        }
        if (booking.getStatus() != StatusType.WAITING) {
            throw new StatusTypeException("Booking with id: " + booking.getId()
                    + " cannot be approved, current booking status: " + booking.getStatus());
        }
        booking.setStatus(isApproved ? StatusType.APPROVED : StatusType.REJECTED);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getById(Long userId, Long bookingId) {
        User user = checkUser(userId);
        Booking booking = checkBooking(bookingId);
        if ((userId.equals(booking.getBooker().getId()) || (userId.equals(booking.getItem().getOwner().getId())))) {
            return BookingMapper.toBookingResponseDto(booking);
        }
        throw new NotFoundException("User with id: " + userId + " is not owner or booker");
    }

    @Override
    public List<BookingResponseDto> getBookingsByBookerIdAndState(Long bookerId, String state, Pageable pageable) {
        StateType stateType = checkStateType(state);
        User user = checkUser(bookerId);
        LocalDateTime now = LocalDateTime.now();
        switch (stateType) {
            case CURRENT:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeAsc(
                                bookerId, now, now, pageable));
            case PAST:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
                                bookerId, now, now, pageable));
            case FUTURE:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByBookerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
                                bookerId, now, now, pageable));
            case WAITING:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartTimeDesc(
                                bookerId, StatusType.WAITING, pageable));
            case REJECTED:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartTimeDesc(
                                bookerId, StatusType.REJECTED, pageable));
            default:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByBookerIdAndOrderByStartTimeDesc(bookerId, pageable));
        }
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwnerAndState(Long ownerId, String state, Pageable pageable) {
        StateType stateType = checkStateType(state);
        User user = checkUser(ownerId);
        LocalDateTime now = LocalDateTime.now();
        switch (stateType) {
            case CURRENT:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfterOrderByStartTimeDesc(
                                ownerId, now, now, pageable));
            case PAST:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsBeforeOrderByStartTimeDesc(
                                ownerId, now, now, pageable));
            case FUTURE:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStartTimeIsAfterAndEndTimeIsAfterOrderByStartTimeDesc(
                                ownerId, now, now, pageable));
            case WAITING:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartTimeDesc(
                                ownerId, StatusType.WAITING, pageable));
            case REJECTED:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartTimeDesc(
                                ownerId, StatusType.REJECTED, pageable));
            default:
                return BookingMapper.toBookingResponseDtoList(bookingRepository
                        .findAllByItemOwnerIdOrderByStartTimeDesc(ownerId, pageable));
        }
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));
    }

    private Booking checkBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Not found booking with id: " + bookingId));
    }

    private StateType checkStateType(String state) {
        return StateType.from(state)
                .orElseThrow(() -> new StatusTypeException("Unknown state: " + state));
    }
}
