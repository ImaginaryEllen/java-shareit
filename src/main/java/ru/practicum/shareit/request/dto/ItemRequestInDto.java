package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestInDto {
    private Long id;
    @NotBlank(groups = Create.class)
    private String description;
    private Long requestorId;
    private LocalDateTime created;
}
