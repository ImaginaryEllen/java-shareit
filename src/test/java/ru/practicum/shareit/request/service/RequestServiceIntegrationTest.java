package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Item request service integration")
class RequestServiceIntegrationTest {
    private final EntityManager em;
    private final ItemRequestServiceImpl itemRequestService;
    private final UserServiceImpl userService;

    @Test
    @DisplayName("Should create item request")
    void shouldCreateItemRequest() {
        UserDto requestor = userService.create(new UserDto(0L, "Dmitry", "dimas@mail.com"));
        ItemRequestOutDto outDto = itemRequestService.create(requestor.getId(),
                new ItemRequestInDto(0L, "boat", requestor.getId(), LocalDateTime.now()));

        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest as r where r.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", outDto.getId()).getSingleResult();

        assertNotNull(outDto, "Return item request dto is null");
        assertNotNull(itemRequest, "Return item request is null");
        assertThat(itemRequest.getId(), equalTo(outDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(outDto.getDescription()));
        assertThat(itemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                equalTo(outDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        assertThat(itemRequest.getRequestor().getId(), equalTo(requestor.getId()));
    }

    @Test
    @DisplayName("Should get item requests by ID")
    void shouldGetItemRequestsById() {
        UserDto requestor = userService.create(new UserDto(0L, "Vicky", "vicky@mail.com"));
        itemRequestService.create(requestor.getId(),
                new ItemRequestInDto(0L, "bike", requestor.getId(), LocalDateTime.now()));

        List<ItemRequestOutDto> outDtoList = itemRequestService.getAllById(requestor.getId());
        ItemRequestOutDto dto = outDtoList.get(0);

        TypedQuery<ItemRequest> query = em.createQuery(
                "select r from ItemRequest as r where r.requestor.id = :id", ItemRequest.class);
        query.setParameter("id", requestor.getId());

        List<ItemRequest> itemRequests = query.getResultList();
        ItemRequest itemRequest = itemRequests.get(0);

        assertNotNull(dto, "Return item request dto is null");
        assertNotNull(itemRequest, "Return item request is null");
        assertThat(itemRequest.getId(), equalTo(dto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(dto.getDescription()));
        assertThat(itemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                equalTo(dto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        assertThat(itemRequest.getRequestor().getId(), equalTo(requestor.getId()));
    }
}