package ru.practicum.ewmservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewmservice.exception.ConflictObjectException;
import ru.practicum.ewmservice.exception.InvalidValueException;
import ru.practicum.ewmservice.exception.NotObjectException;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notObject(final NotObjectException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), "NotObjectException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflict(final ConflictObjectException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.CONFLICT, e.getMessage(), "ConflictObjectException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleError(final Throwable e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Throwable");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse argumentNot(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "MethodArgumentNotValidException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse value(final InvalidValueException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "InvalidValueException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse validated(final ConstraintViolationException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.CONFLICT, e.getMessage(), "ConstraintViolationException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validated(final MethodArgumentTypeMismatchException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "MethodArgumentTypeMismatchException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validated(final NullPointerException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "NullPointerException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse validated(final DataIntegrityViolationException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.CONFLICT, e.getMessage(), "DataIntegrityViolationException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validated(final MissingServletRequestParameterException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(),
                "MissingServletRequestParameterException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validated(final UnexpectedTypeException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "UnexpectedTypeException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validated(final ConstraintDeclarationException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "ConstraintDeclarationException");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validated(final HttpMessageNotReadableException e) {
        log.warn(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), " HttpMessageNotReadableException");
    }
}
