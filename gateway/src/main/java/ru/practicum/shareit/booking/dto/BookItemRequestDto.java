package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.StartBeforeEnd;
import ru.practicum.shareit.validation.Timespan;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEnd
public class BookItemRequestDto implements Timespan {
    private long itemId;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
