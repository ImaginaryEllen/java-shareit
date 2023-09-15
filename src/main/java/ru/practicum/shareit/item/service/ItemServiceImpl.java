package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.CommentMapper;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemInfoDto;
import ru.practicum.shareit.item.dto.item.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = checkUser(userId);
        List<Comment> comments = new ArrayList<>();
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, owner, comments)));
    }

    @Transactional
    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        Item item = checkItem(itemId);
        itemDto.setId(itemId);
        User owner = checkUser(userId);
        if (!owner.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("User with id: " + userId + " have not item with id: " + itemId);
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemInfoDto getById(Long userId, Long itemId) {
        return getItemInfoDto(itemId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemInfoDto> getAllItems(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerIdOrderById(ownerId);
        return items.stream().map(item -> getItemInfoDto(item.getId(), ownerId)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return ItemMapper.toItemDtoList(itemRepository.search(text));
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Item item = checkItem(itemId);
        User author = checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findBookingsByItemIdAndBookerId(itemId, userId).stream()
                .filter(booking -> booking.getEndTime().isBefore(now))
                .filter(booking -> booking.getStatus().equals(StatusType.APPROVED))
                .collect(Collectors.toList());
        if (bookings.size() == 0) {
            throw new ValidationException("User with id: " + userId + " don`t booking item with id: " + itemId);
        }
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Comment text is blank");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, author, now);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private ItemInfoDto getItemInfoDto(Long itemId, Long userId) {
        Item item = checkItem(itemId);
        User user = checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<CommentDto> comments = CommentMapper.toCommentDtoList(commentRepository.findCommentsByItemId(itemId));
        Booking lastBooking = bookingRepository.findLastBookingByItemId(itemId, userId, now).stream()
                .filter(booking -> booking.getStatus().equals(StatusType.APPROVED))
                .findFirst()
                .orElse(null);
        Booking nextBooking = bookingRepository.findNextBookingByItemId(itemId, userId, now).stream()
                .filter(booking -> booking.getStatus().equals(StatusType.APPROVED))
                .findFirst()
                .orElse(null);
        return ItemMapper.toItemInfoDto(item, lastBooking, nextBooking, comments);
    }

    private Item checkItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Not found item with id: " + itemId));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));
    }
}
