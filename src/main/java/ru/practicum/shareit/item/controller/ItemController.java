package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Validated(Create.class)
    public ItemDto create(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Create item: {} - STARTED", item);
        ItemDto itemDto = itemService.create(item, userId);
        log.info("Create item: {} - FINISHED", itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    @Validated(Update.class)
    public ItemDto update(@PathVariable Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto item) {
        log.info("Update user: {} - STARTED", item);
        ItemDto itemDto = itemService.update(itemId, userId, item);
        log.info("Update user: {} - FINISHED", itemDto);
        return itemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Long itemId) {
        log.info("Get item by id: {}", itemId);
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items by user id: {}", userId);
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(value = "text", required = false) String text) {
        log.info("Search items by text: {}", text);
        return itemService.search(text);
    }
}
