package ru.practicum.ewmservice.exception;

public class ConflictObjectException extends RuntimeException {
    public ConflictObjectException(String massage) {
        super(massage);
    }
}
