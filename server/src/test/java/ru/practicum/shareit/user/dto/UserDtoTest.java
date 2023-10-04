package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("User dto json")
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> jacksonTester;
    private final UserDto userDto = new UserDto(1L, "Bella", "bell@mail.com");

    @Test
    @DisplayName("should serialize user dto")
    void testUserDtoSerialize() throws Exception {
        var json = jacksonTester.write(userDto);

        assertThat(json).hasJsonPath("$.id");
        assertThat(json).hasJsonPath("$.name");
        assertThat(json).hasJsonPath("$.email");

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

    @Test
    @DisplayName("should deserialize user dto")
    void testUserDtoDeserialize() throws Exception {
        var user = new UserDto(1L, "Nick", "nicky@mail.com");

        String dtoJson = "{ \"id\":1, \"name\":\"Nick\", \"email\":\"nicky@mail.com\"}";

        var dto = jacksonTester.parseObject(dtoJson);

        assertThat(dto).extracting("id").isEqualTo(user.getId());
        assertThat(dto).extracting("name").isEqualTo(user.getName());
        assertThat(dto).extracting("email").isEqualTo(user.getEmail());
    }
}