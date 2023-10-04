package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constant.Constant.USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID) Long userId,
                                         @Validated(Create.class) @RequestBody ItemRequestInDto itemRequestDto) {
        log.info("Create request: {} - STARTED", itemRequestDto);
        return requestClient.create(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(USER_ID) Long userId, @PathVariable Long requestId) {
        log.info("Getting request by id: {} for user with id: {}", requestId, userId);
        return requestClient.getById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllById(@RequestHeader(USER_ID) Long userId) {
        log.info("Getting requests for user with id: {}", userId);
        return requestClient.getAllById(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID) Long userId,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Getting all requests for user with id: {}", userId);
        return requestClient.getAll(userId, from, size);
    }
}
