package ru.practicum.shareit.item.dto.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("Item dto json")
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> jacksonTester;
    private final ItemDto itemDto = new ItemDto(
            1L, "Camera", "Canon R100", Boolean.TRUE, null);

    @Test
    @DisplayName("should serialize item dto")
    void testItemDtoSerialize() throws Exception {
        var json = jacksonTester.write(itemDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.name");
        assertThat(json).hasJsonPath("$.description");
        assertThat(json).hasJsonPath("$.available");
        assertThat(json).hasJsonPath("$.requestId");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDto.getRequestId());
    }

    @Test
    @DisplayName("should deserialize item dto")
    void testItemDtoDeserialize() throws Exception {
        String dtoJson = "{ \"id\":1, \"name\":\"Camera\", \"description\":\"Canon R100\"," +
                " \"available\":true, \"requestId\":null}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(1L);
        assertThat(dto).extracting("name").isEqualTo(itemDto.getName());
        assertThat(dto).extracting("description").isEqualTo(itemDto.getDescription());
        assertThat(dto).extracting("available").isEqualTo(itemDto.getAvailable());
        assertThat(dto).extracting("requestId").isEqualTo(itemDto.getRequestId());
    }
}