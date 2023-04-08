package ru.practicum.ewmservice.valide;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateConstraintValidator.class)
public @interface NowBeforeDate {
    String message() default "{NowBeforeDate}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int hours() default 0;

}
