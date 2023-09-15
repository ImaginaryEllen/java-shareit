package ru.practicum.shareit.booking.type;

import java.util.Optional;

public enum StateType {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<StateType> from(String state) {
        for (StateType value : StateType.values()) {
            if (value.name().equals(state)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
