package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Item service integration")
class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Test
    @Order(value = 1)
    @DisplayName("should create item from repository")
    void shouldCreateItem() {
        UserDto userDto = userService.create(new UserDto(0L, "Martin", "marty@mail.com"));
        ItemDto itemDto = itemService.create(new ItemDto(0L, "Drill", "electric drill",
                true, null), userDto.getId());

        TypedQuery<Item> query = em.createQuery("select i from Item as i where i.id = :id", Item.class);
        Item item = query.setParameter("id", itemDto.getId()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }

    @Test
    @Order(value = 2)
    @DisplayName("should get all items from repository")
    void shouldGetAllItems() {
        UserDto userDto = userService.create(new UserDto(0L, "Maria", "mary@mail.com"));
        itemService.create(new ItemDto(0L, "Flashlight", "for pocket",
                true, null), userDto.getId());
        List<ItemInfoDto> itemInfoDtoList = itemService.getAllItems(userDto.getId(), Pageable.ofSize(10));
        ItemInfoDto itemInfoDto = itemInfoDtoList.get(0);

        TypedQuery<Item> query = em.createQuery("select i from Item as i where i.owner.id = :id", Item.class);
        query.setParameter("id", userDto.getId());

        List<Item> items = query.getResultList();
        Item item = items.get(0);

        assertThat(items.size(), equalTo(itemInfoDtoList.size()));
        assertThat(item.getId(), notNullValue());
        assertThat(item.getId(), equalTo(itemInfoDto.getId()));
        assertThat(item.getName(), equalTo(itemInfoDto.getName()));
        assertThat(item.getDescription(), equalTo(itemInfoDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemInfoDto.getAvailable()));
    }
}