package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("Booking dto json")
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> jacksonTester;
    private final LocalDateTime start = LocalDateTime.of(2023, Month.OCTOBER, 15, 15, 15);
    private final LocalDateTime end = LocalDateTime.of(2023, Month.OCTOBER, 25, 15, 15);
    private final BookingDto bookingDto = new BookingDto(5L, start, end, 5L);

    @Test
    @DisplayName("should serialize booking dto")
    void testBookingDtoSerialize() throws Exception {
        var json = jacksonTester.write(bookingDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.start");
        assertThat(json).hasJsonPath("$.end");
        assertThat(json).hasJsonPath("$.bookerId");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDto.getId().intValue());
        assertThat(json).extractingJsonPathValue("$.start").isEqualTo(bookingDto.getStart()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(json).extractingJsonPathValue("$.end").isEqualTo(bookingDto.getEnd()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(json).extractingJsonPathNumberValue("$.bookerId").isEqualTo(bookingDto.getBookerId().intValue());
    }

    @Test
    @DisplayName("should deserialize booking dto")
    void testBookingDtoDeserialize() throws Exception {
        String dtoJson = "{ \"id\":5, \"start\":\"" + start + "\", \"end\":\"" + end + "\", \"bookerId\":5}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(bookingDto.getId());
        assertThat(dto).extracting("start").isEqualTo(bookingDto.getStart());
        assertThat(dto).extracting("end").isEqualTo(bookingDto.getEnd());
        assertThat(dto).extracting("bookerId").isEqualTo(bookingDto.getBookerId());
    }
}