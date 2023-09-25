package ru.practicum.shareit.item.dto.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("Comment dto json")
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> jacksonTester;
    private final LocalDateTime date =
            LocalDateTime.of(2023, Month.APRIL, 12, 12, 10, 10);
    private final CommentDto commentDto = new CommentDto(1L, "nice", "Sally", date);

    @Test
    @DisplayName("should serialize comment dto")
    void testCommentDtoSerialize() throws Exception {
        var json = jacksonTester.write(commentDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.text");
        assertThat(json).hasJsonPath("$.authorName");
        assertThat(json).hasJsonPath("$.created");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(json).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(json).extractingJsonPathValue("$.created").isEqualTo(commentDto.getCreated()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Test
    @DisplayName("should deserialize comment dto")
    void testCommentDtoDeserialize() throws Exception {
        String dtoJson = "{ \"id\":1, \"text\":\"nice\", \"authorName\":\"Sally\", \"created\":\"" + date + "\"}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(commentDto.getId());
        assertThat(dto).extracting("text").isEqualTo(commentDto.getText());
        assertThat(dto).extracting("authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(dto).extracting("created").isEqualTo(commentDto.getCreated());
    }
}