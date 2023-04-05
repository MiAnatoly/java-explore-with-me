package ru.practicum.ewmservice;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final String reason;
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    public ErrorResponse(HttpStatus httpStatus, String message, String reason) {
        this.status = httpStatus;
        this.message = message;
        this.reason = reason;
    }

}
