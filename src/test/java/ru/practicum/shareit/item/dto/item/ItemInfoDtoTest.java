package ru.practicum.shareit.item.dto.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("Item info dto json")
class ItemInfoDtoTest {
    @Autowired
    private JacksonTester<ItemInfoDto> jacksonTester;
    private final ItemInfoDto itemInfoDto = new ItemInfoDto(1L, "Brush", "brush for paints",
            Boolean.TRUE, null, null, new ArrayList<>());

    @Test
    @DisplayName("should serialize item info dto")
    void testItemInfoDtoSerialize() throws Exception {
        var json = jacksonTester.write(itemInfoDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.name");
        assertThat(json).hasJsonPath("$.description");
        assertThat(json).hasJsonPath("$.available");
        assertThat(json).hasJsonPath("$.lastBooking");
        assertThat(json).hasJsonPath("$.nextBooking");
        assertThat(json).hasJsonPath("$.comments");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(itemInfoDto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(itemInfoDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(itemInfoDto.getDescription());
        assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(itemInfoDto.getAvailable());
        assertThat(json).extractingJsonPathValue("$.lastBooking").isEqualTo(itemInfoDto.getLastBooking());
        assertThat(json).extractingJsonPathValue("$.nextBooking").isEqualTo(itemInfoDto.getNextBooking());
        assertThat(json).extractingJsonPathValue("$.comments").isEqualTo(itemInfoDto.getComments());
    }

    @Test
    @DisplayName("should deserialize item info dto")
    void testItemInfoDtoDeserialize() throws Exception {
        String dtoJson = "{ \"id\":1, \"name\":\"Brush\", \"description\":\"brush for paints\"," +
                " \"available\":true, \"lastBooking\":null, \"nextBooking\":null, \"comments\":[]}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(1L);
        assertThat(dto).extracting("name").isEqualTo(itemInfoDto.getName());
        assertThat(dto).extracting("description").isEqualTo(itemInfoDto.getDescription());
        assertThat(dto).extracting("available").isEqualTo(itemInfoDto.getAvailable());
        assertThat(dto).extracting("lastBooking").isEqualTo(itemInfoDto.getLastBooking());
        assertThat(dto).extracting("nextBooking").isEqualTo(itemInfoDto.getNextBooking());
        assertThat(dto).extracting("comments").isEqualTo(itemInfoDto.getComments());
    }
}