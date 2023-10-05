package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.CommentMapper;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemInfoDto;
import ru.practicum.shareit.item.dto.item.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Item service")
class ItemServiceImplTest {
    ItemServiceImpl itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;
    BookingRepository bookingRepository;
    ItemRequestRepository itemRequestRepository;
    Item item;
    User owner;
    User requestor;
    ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        commentRepository = mock(CommentRepository.class);
        bookingRepository = mock(BookingRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository, userRepository, commentRepository, bookingRepository, itemRequestRepository);
        owner = new User(1L, "Bill", "bill@mail.com");
        requestor = new User(2L, "Teodora", "teo@mail.com");
        itemRequest = new ItemRequest(1L, "text", requestor, LocalDateTime.now());
        item = new Item(1L, "Paints", "watercolour", true, owner,
                new ArrayList<>(), itemRequest);
    }

    @Test
    @DisplayName("Should create item")
    void shouldCreateItem() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any()))
                .thenReturn(item);

        ItemDto itemDto = itemService.create(ItemMapper.toItemDto(item), owner.getId());

        assertThat(itemDto.getId(), equalTo(item.getId()));
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(itemDto.getRequestId(), equalTo(item.getItemRequest().getId()));
    }

    @Test
    @DisplayName("Should update item")
    void shouldUpdateItem() {
        Item updateItem = new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getOwner(), item.getComments(), item.getItemRequest());
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(any()))
                .thenReturn(item);

        ItemDto itemDto = itemService.update(item.getId(), owner.getId(), ItemMapper.toItemDto(updateItem));

        assertThat(itemDto.getId(), equalTo(updateItem.getId()));
        assertThat(itemDto.getName(), equalTo(updateItem.getName()));
        assertThat(itemDto.getDescription(), equalTo(updateItem.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(updateItem.getAvailable()));
        assertThat(itemDto.getRequestId(), equalTo(updateItem.getItemRequest().getId()));
    }

    @Test
    @DisplayName("Should get item by ID")
    void shouldGetItemById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));

        ItemInfoDto itemInfoDto = ItemMapper.toItemInfoDto(item, null, null, new ArrayList<>());
        ItemInfoDto returnDto = itemService.getById(owner.getId(), item.getId());

        assertNotNull(returnDto, "Return item dto is null");
        assertThat(itemInfoDto.getId(), equalTo(returnDto.getId()));
        assertThat(itemInfoDto.getName(), equalTo(returnDto.getName()));
        assertThat(itemInfoDto.getDescription(), equalTo(returnDto.getDescription()));
        assertThat(itemInfoDto.getAvailable(), equalTo(returnDto.getAvailable()));
    }

    @Test
    @DisplayName("Should get all items")
    void shouldGetAllItems() {
        Pageable pageable = PageRequest.ofSize(10);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(itemRepository.findByOwnerIdOrderById(1L, pageable))
                .thenReturn(Collections.singletonList(item));

        List<ItemInfoDto> itemInfoDtoList = itemService.getAllItems(1L, pageable);
        ItemInfoDto returnDto = itemInfoDtoList.get(0);

        assertNotNull(itemInfoDtoList, "Return list is null");
        assertNotNull(returnDto, "Return item dto is null");
        assertEquals(1, itemInfoDtoList.size(),
                "Incorrect size: expected 1, actual - " + itemInfoDtoList.size());
        assertThat(item.getId(), equalTo(returnDto.getId()));
        assertThat(item.getName(), equalTo(returnDto.getName()));
        assertThat(item.getDescription(), equalTo(returnDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(returnDto.getAvailable()));
    }

    @Test
    @DisplayName("Should search item by text")
    void shouldSearchItem() {
        Pageable pageable = PageRequest.ofSize(10);
        String text = "paints";
        when(itemRepository.search(text, pageable))
                .thenReturn(Collections.singletonList(item));

        List<ItemDto> itemDtoList = itemService.search(text, pageable);
        ItemDto returnDto = itemDtoList.get(0);

        assertNotNull(itemDtoList, "Return list is null");
        assertNotNull(returnDto, "Return item dto is null");
        assertEquals(1, itemDtoList.size(),
                "Incorrect size: expected 1, actual - " + itemDtoList.size());
        assertThat(item.getId(), equalTo(returnDto.getId()));
        assertThat(item.getName(), equalTo(returnDto.getName()));
        assertThat(item.getDescription(), equalTo(returnDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(returnDto.getAvailable()));
    }

    @Test
    @DisplayName("Should add new comment")
    void shouldAddComment() {
        Comment comment = new Comment(1L, "good", item, requestor, LocalDateTime.now());
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(1),
                item, requestor, StatusType.APPROVED);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findBookingsByItemIdAndBookerId(anyLong(), anyLong()))
                .thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto commentDto =
                itemService.addComment(requestor.getId(), item.getId(), CommentMapper.toCommentDto(comment));

        assertNotNull(commentDto, "Return comment dto is null");
        assertThat(comment.getId(), equalTo(commentDto.getId()));
        assertThat(comment.getText(), equalTo(commentDto.getText()));
        assertThat(requestor.getName(), equalTo(commentDto.getAuthorName()));
    }
}