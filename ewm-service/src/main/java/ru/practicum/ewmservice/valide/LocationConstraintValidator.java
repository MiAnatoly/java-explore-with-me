package ru.practicum.ewmservice.valide;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class LocationConstraintValidator implements ConstraintValidator<Location, Float> {
    private float min;
    private float max;

    @Override
    public void initialize(Location nowBeforeDate) {
        this.min = nowBeforeDate.min();
        this.max = nowBeforeDate.max();
    }

    @Override
    public boolean isValid(Float loc, ConstraintValidatorContext context) {
        if (loc == null) {
            return false;
        }
        return loc > min && loc < max;
    }
}
