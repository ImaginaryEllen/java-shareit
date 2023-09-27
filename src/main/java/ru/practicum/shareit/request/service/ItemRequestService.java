package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestOutDto create(Long userId, ItemRequestInDto itemRequestDto);

    ItemRequestOutDto getById(Long userId, Long requestId);

    List<ItemRequestOutDto> getAllById(Long userId);

    List<ItemRequestOutDto> getAll(Long userId, Pageable pageable);
}
