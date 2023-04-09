package ru.practicum.ewmservice.valide;

import ru.practicum.ewmservice.model.comments.Comment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateCommentConstraintValidator implements ConstraintValidator<DateCreatedHourAfterUpdate, Comment> {

    @Override
    public void initialize(DateCreatedHourAfterUpdate startBeforeEnd) {
    }

    @Override
    public boolean isValid(Comment comment, ConstraintValidatorContext context) {
        LocalDateTime created = comment.getCreated();
        LocalDateTime update = comment.getUpdated();
        if (update == null) {
            return true;
        }
        return created.plusHours(1).isAfter(update);
    }
}
