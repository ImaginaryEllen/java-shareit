package ru.practicum.shareit.exception;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ErrorResponse {
    @NotNull
    private final String message;
}
