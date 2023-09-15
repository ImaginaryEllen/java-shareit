package ru.practicum.shareit.validation;

import java.time.LocalDateTime;

public interface Timespan {
    LocalDateTime getStart();

    LocalDateTime getEnd();
}
