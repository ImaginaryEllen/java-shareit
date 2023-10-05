package ru.practicum.shareit.item.dto.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Item mapper")
class ItemMapperTest {
    User owner;
    Item item;
    ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        owner = new User(10L, "Patrik", "ppp@mail.com");
        item = new Item(10L, "Batman", "Collection DC Comics", true,
                owner, new ArrayList<>(), null);
        itemDto = new ItemDto(11L, "Camera Canon", "EOS 5D Mark IV", Boolean.TRUE, null);
    }

    @Test
    @DisplayName("should mapping item -> itemDto")
    void shouldToItemDto() {
        ItemDto dto = ItemMapper.toItemDto(item);

        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(item.getId()));
        assertThat(dto.getName(), equalTo(item.getName()));
        assertThat(dto.getDescription(), equalTo(item.getDescription()));
        assertThat(dto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    @DisplayName("should mapping itemDto -> item")
    void shouldToItem() {
        Item newItem = ItemMapper.toItem(itemDto, owner, new ArrayList<>(), null);

        assertNotNull(newItem, "New item is null");
        assertThat(newItem.getId(), equalTo(itemDto.getId()));
        assertThat(newItem.getName(), equalTo(itemDto.getName()));
        assertThat(newItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(newItem.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(newItem.getOwner().getId(), equalTo(owner.getId()));
    }

    @Test
    @DisplayName("should mapping item list -> itemDto list")
    void shouldToItemDtoList() {
        List<Item> items = List.of(item);
        List<ItemDto> itemDtoList = ItemMapper.toItemDtoList(items);
        ItemDto dto = itemDtoList.get(0);

        assertEquals(items.size(), itemDtoList.size(),
                "Incorrect size: expected " + items.size() + ", actual - " + itemDtoList.size());
        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(item.getId()));
        assertThat(dto.getName(), equalTo(item.getName()));
        assertThat(dto.getDescription(), equalTo(item.getDescription()));
        assertThat(dto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    @DisplayName("should mapping item -> item info dto")
    void shouldToItemInfoDto() {
        ItemInfoDto dto = ItemMapper.toItemInfoDto(item, null, null, new ArrayList<>());

        assertNotNull(dto, "Dto is null");
        assertThat(dto.getId(), equalTo(item.getId()));
        assertThat(dto.getName(), equalTo(item.getName()));
        assertThat(dto.getDescription(), equalTo(item.getDescription()));
        assertThat(dto.getAvailable(), equalTo(item.getAvailable()));
    }
}