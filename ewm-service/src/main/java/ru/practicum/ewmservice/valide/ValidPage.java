package ru.practicum.ewmservice.valide;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.exception.InvalidValueException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidPage {
    public static Integer page(Integer from, Integer size) {
        if (from % size == 0) {
            return from / size;
        } else {
            throw new InvalidValueException("передан не первый элемент страницы");
        }
    }
}
