package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequestOutDto create(Long userId, ItemRequestInDto itemRequestDto) {
        User user = checkUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(user, itemRequestDto);
        return ItemRequestMapper.toItemRequestOutDto(itemRequestRepository.save(itemRequest), new ArrayList<>());
    }

    @Override
    public ItemRequestOutDto getById(Long userId, Long requestId) {
        User user = checkUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Not found request with id: " + requestId));
        List<ItemDto> items = ItemMapper.toItemDtoList(itemRepository.findAllByItemRequestId(requestId));
        return ItemRequestMapper.toItemRequestOutDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestOutDto> getAllById(Long userId) {
        User user = checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);
        return getItemResponseDtoList(itemRequests);
    }

    @Override
    public List<ItemRequestOutDto> getAll(Long userId, Pageable pageable) {
        User user = checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserId(userId, pageable);
        return getItemResponseDtoList(itemRequests);
    }

    private List<ItemRequestOutDto> getItemResponseDtoList(List<ItemRequest> itemRequests) {
        List<ItemRequestOutDto> itemResponseDto = new ArrayList<>();
        if (!itemRequests.isEmpty()) {
            for (ItemRequest itemRequest : itemRequests) {
                List<ItemDto> itemDto = ItemMapper.toItemDtoList(itemRepository.findAllByItemRequestId(itemRequest.getId()));
                itemResponseDto.add(ItemRequestMapper.toItemRequestOutDto(itemRequest, itemDto));
            }
        }
        return itemResponseDto;
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + userId));
    }
}
