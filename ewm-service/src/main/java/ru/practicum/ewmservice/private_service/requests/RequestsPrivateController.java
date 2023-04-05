package ru.practicum.ewmservice.private_service.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.private_service.requests.service.RequestsPrivateService;

import java.util.Optional;

@Controller
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestsPrivateController {
    private final RequestsPrivateService service;

    @GetMapping
    public ResponseEntity<Object> findByUser(
            @PathVariable Long userId
    ) {
        log.info("get all a request by userId:{}", userId);
        return ResponseEntity.of(Optional.of(service.findByUser(userId)));
    }

    @PostMapping
    public ResponseEntity<Object> add(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        log.info("post a eventId:{} to userId:{}", eventId, userId);
        return new ResponseEntity<>(service.add(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        log.info("cancel requestId:{} to userId:{}", requestId, userId);
        return new ResponseEntity<>(service.cancelRequest(userId, requestId), HttpStatus.OK);
    }
}
