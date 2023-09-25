package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.constant.Constant.USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto item,
                          @RequestHeader(USER_ID) Long userId) {
        log.info("Create item: {} - STARTED", item);
        ItemDto itemDto = itemService.create(item, userId);
        log.info("Create item: {} - FINISHED", itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestHeader(USER_ID) Long userId, @Validated @RequestBody ItemDto item) {
        log.info("Update user: {} - STARTED", item);
        ItemDto itemDto = itemService.update(itemId, userId, item);
        log.info("Update user: {} - FINISHED", itemDto);
        return itemDto;
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto get(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId) {
        log.info("Get item by id: {} and by owner id: {}", itemId, userId);
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemInfoDto> getAll(@RequestHeader(USER_ID) Long userId,
                                    @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Get all items by user id: {}", userId);
        return itemService.getAllItems(userId, PageRequest.of(from > 0 ? from / size : 0, size));
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(value = "text") String text,
                                @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Search items by text: {}", text);
        return itemService.search(text, PageRequest.of(from > 0 ? from / size : 0, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_ID) Long userId,
                                 @PathVariable Long itemId, @RequestBody CommentDto comment) {
        log.info("Create comment: {} - STARTED from user with id: {} for item with id: {} ", comment, userId, itemId);
        CommentDto newComment = itemService.addComment(userId, itemId, comment);
        log.info("Create comment: {} - FINISHED", newComment);
        return newComment;
    }
}