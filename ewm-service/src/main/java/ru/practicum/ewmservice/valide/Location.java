package ru.practicum.ewmservice.valide;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocationConstraintValidator.class)
public @interface Location {
    String message() default "{Location}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    float min() default -180.0f;
    float max() default 180.0f;
}
