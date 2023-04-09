package ru.practicum.ewmservice.valide;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateCommentConstraintValidator.class)
public @interface DateCreatedHourAfterUpdate {
    String message() default "{DateCreatedHourAfterUpdate}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
