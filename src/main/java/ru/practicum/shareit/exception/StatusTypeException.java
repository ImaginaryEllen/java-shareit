package ru.practicum.shareit.exception;

public class StatusTypeException extends IllegalArgumentException {
    public StatusTypeException(String message) {
        super(message);
    }
}