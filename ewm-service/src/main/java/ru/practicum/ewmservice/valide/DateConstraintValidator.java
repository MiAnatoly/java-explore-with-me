package ru.practicum.ewmservice.valide;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDateTime;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class DateConstraintValidator implements ConstraintValidator<NowBeforeDate, LocalDateTime> {
private int hours;

    @Override
    public void initialize(NowBeforeDate nowBeforeDate) {
        this.hours = nowBeforeDate.hours();
    }

    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext context) {
        if (date == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(hours).isBefore(date);
    }
}
