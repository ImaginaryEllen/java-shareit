package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("Item request dto out json")
class ItemRequestOutDtoTest {
    @Autowired
    private JacksonTester<ItemRequestOutDto> jacksonTester;
    private final LocalDateTime date = LocalDateTime.of(2023, Month.SEPTEMBER, 15, 15, 15);
    private final ItemRequestOutDto outDto = new ItemRequestOutDto(5L, "boat", date, new ArrayList<>());

    @Test
    @DisplayName("should serialize item request dto out")
    void testItemRequestOutDtoSerialize() throws Exception {
        var json = jacksonTester.write(outDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.description");
        assertThat(json).hasJsonPath("$.created");
        assertThat(json).hasJsonPath("$.items");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(outDto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(outDto.getDescription());
        assertThat(json).extractingJsonPathValue("$.created").isEqualTo(outDto.getCreated()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(json).extractingJsonPathValue("$.items").isEqualTo(outDto.getItems());

    }

    @Test
    @DisplayName("should deserialize item request dto out")
    void testItemRequestOutDtoDeserialize() throws Exception {
        String dtoJson = "{ \"id\":5, \"description\":\"boat\", \"created\":\"" + date + "\", \"items\":[]}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(outDto.getId());
        assertThat(dto).extracting("description").isEqualTo(outDto.getDescription());
        assertThat(dto).extracting("created").isEqualTo(outDto.getCreated());
        assertThat(dto).extracting("items").isEqualTo(outDto.getItems());
    }
}