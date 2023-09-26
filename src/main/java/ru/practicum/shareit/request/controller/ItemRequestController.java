package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.constant.Constant.USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestOutDto create(@RequestHeader(USER_ID) Long userId,
                                    @Validated(Create.class) @RequestBody ItemRequestInDto itemRequestDto) {
        log.info("Create request: {} - STARTED", itemRequestDto);
        ItemRequestOutDto itemResponseDto = itemRequestService.create(userId, itemRequestDto);
        log.info("Create request: {} - FINISHED", itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutDto getItemRequestById(@RequestHeader(USER_ID) Long userId, @PathVariable Long requestId) {
        log.info("Getting request by id: {} for user with id: {}", requestId, userId);
        return itemRequestService.getById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestOutDto> getAllById(@RequestHeader(USER_ID) Long userId) {
        log.info("Getting requests for user with id: {}", userId);
        return itemRequestService.getAllById(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutDto> getAll(@RequestHeader(USER_ID) Long userId,
                                          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Getting all requests for user with id: {}", userId);
        return itemRequestService.getAll(userId, PageRequest.of(from > 0 ? from / size : 0, size));
    }
}
