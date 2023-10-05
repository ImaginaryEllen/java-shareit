package ru.practicum.shareit.request.dto;

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
@DisplayName("Item request dto in json")
class ItemRequestInDtoTest {
    @Autowired
    private JacksonTester<ItemRequestInDto> jacksonTester;
    private final LocalDateTime date = LocalDateTime.of(2023, Month.SEPTEMBER, 15, 15, 15);
    private final ItemRequestInDto inDto = new ItemRequestInDto(5L, "boat", 5L, date);

    @Test
    @DisplayName("should serialize item request dto in")
    void testItemRequestInDtoSerialize() throws Exception {
        var json = jacksonTester.write(inDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.description");
        assertThat(json).hasJsonPath("$.requestorId");
        assertThat(json).hasJsonPath("$.created");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(inDto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(inDto.getDescription());
        assertThat(json).extractingJsonPathNumberValue("$.requestorId").isEqualTo(inDto.getRequestorId().intValue());
        assertThat(json).extractingJsonPathValue("$.created").isEqualTo(inDto.getCreated()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    }

    @Test
    @DisplayName("should deserialize item request dto in")
    void testItemRequestInDtoDeserialize() throws Exception {
        String dtoJson = "{ \"id\":5, \"description\":\"boat\", \"requestorId\":5, \"created\":\"" + date + "\"}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(inDto.getId());
        assertThat(dto).extracting("description").isEqualTo(inDto.getDescription());
        assertThat(dto).extracting("requestorId").isEqualTo(inDto.getRequestorId());
        assertThat(dto).extracting("created").isEqualTo(inDto.getCreated());
    }
}