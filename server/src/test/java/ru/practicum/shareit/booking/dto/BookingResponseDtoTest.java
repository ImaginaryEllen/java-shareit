package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.type.StatusType;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("Booking response dto json")
class BookingResponseDtoTest {
    @Autowired
    private JacksonTester<BookingResponseDto> jacksonTester;

    private final User booker = new User(15L, "Mia", "mia@mail.com");
    private final Item item = new Item(15L, "Pencils", "colored pencils", true, booker,
            new ArrayList<>(), null);
    private final LocalDateTime start = LocalDateTime.of(2023, Month.OCTOBER, 15, 15, 15);
    private final LocalDateTime end = LocalDateTime.of(2023, Month.OCTOBER, 25, 15, 15);
    private final StatusType status = StatusType.WAITING;
    private final BookingResponseDto responseDto = new BookingResponseDto(15L, start, end, status, booker, item);

    @Test
    @DisplayName("should serialize booking response dto")
    void testBookingResponseDtoSerialize() throws Exception {
        var json = jacksonTester.write(responseDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.start");
        assertThat(json).hasJsonPath("$.end");
        assertThat(json).hasJsonPath("$.status");
        assertThat(json).hasJsonPath("$.booker");
        assertThat(json).hasJsonPath("$.item");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(responseDto.getId().intValue());
        assertThat(json).extractingJsonPathValue("$.start").isEqualTo(responseDto.getStart()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(json).extractingJsonPathValue("$.end").isEqualTo(responseDto.getEnd()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(json).extractingJsonPathValue("$.status").isEqualTo(responseDto.getStatus().toString());
        assertThat(json).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(responseDto.getBooker().getId().intValue());
        assertThat(json).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(responseDto.getItem().getId().intValue());
    }

    @Test
    @DisplayName("should deserialize booking response dto")
    void testBookingResponseDtoDeserialize() throws Exception {
        String dtoJson = "{ \"id\":15, \"start\":\"" + start + "\", \"end\":\"" + end + "\", \"status\":\"" + status +
                "\", \"booker.id\":\"" + booker.getId() + "\", \"item.id\":\"" + item.getId() + "\"}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(responseDto.getId());
        assertThat(dto).extracting("start").isEqualTo(responseDto.getStart());
        assertThat(dto).extracting("end").isEqualTo(responseDto.getEnd());
        assertThat(dto).extracting("status").isEqualTo(responseDto.getStatus());
    }
}