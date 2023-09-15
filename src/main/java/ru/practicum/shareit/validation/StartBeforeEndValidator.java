package ru.practicum.shareit.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, Timespan> {
    @Override
    public boolean isValid(Timespan value, ConstraintValidatorContext constraintValidatorContext) {
        return value.getStart().isBefore(value.getEnd());
    }
}
