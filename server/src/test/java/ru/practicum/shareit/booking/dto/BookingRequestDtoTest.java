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
@DisplayName("Booking request dto json")
class BookingRequestDtoTest {
    @Autowired
    private JacksonTester<BookingRequestDto> jacksonTester;
    private final LocalDateTime start = LocalDateTime.of(2023, Month.OCTOBER, 15, 15, 15);
    private final LocalDateTime end = LocalDateTime.of(2023, Month.OCTOBER, 25, 15, 15);
    private final BookingRequestDto requestDto = new BookingRequestDto(15L, start, end);

    @Test
    @DisplayName("should serialize booking request dto")
    void testBookingRequestDtoSerialize() throws Exception {
        var json = jacksonTester.write(requestDto);

        assertThat(json).hasJsonPath("$.itemId");
        assertThat(json).hasJsonPath("$.start");
        assertThat(json).hasJsonPath("$.end");

        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(requestDto.getItemId().intValue());
        assertThat(json).extractingJsonPathValue("$.start").isEqualTo(requestDto.getStart()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(json).extractingJsonPathValue("$.end").isEqualTo(requestDto.getEnd()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Test
    @DisplayName("should deserialize booking request dto")
    void testBookingRequestDtoDeserialize() throws Exception {
        String dtoJson = "{ \"itemId\":15, \"start\":\"" + start + "\", \"end\":\"" + end + "\"}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("itemId").isEqualTo(requestDto.getItemId());
        assertThat(dto).extracting("start").isEqualTo(requestDto.getStart());
        assertThat(dto).extracting("end").isEqualTo(requestDto.getEnd());
    }
}