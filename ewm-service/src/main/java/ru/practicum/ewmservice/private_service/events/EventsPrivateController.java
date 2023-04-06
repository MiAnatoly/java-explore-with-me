package ru.practicum.ewmservice.private_service.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.events.NewEventDto;
import ru.practicum.ewmservice.dto.events.UpdateEventUserRequest;
import ru.practicum.ewmservice.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.private_service.events.service.EventsPrivateService;
import ru.practicum.ewmservice.valide.ValidPage;

import javax.validation.constraints.Min;

@Controller
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventsPrivateController {
    private final EventsPrivateService service;

    @GetMapping
    public ResponseEntity<Object> findByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get users with from={} and size={}", from, size);
        return new ResponseEntity<>(service.findByUser(userId, page, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(
            @PathVariable Long userId,
            @Validated @RequestBody NewEventDto event
    ) {
        log.info("Post event title:{}", event.getTitle());
        return new ResponseEntity<>(service.add(userId, event), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> findById(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("Get userId:{} eventId:{}", userId, eventId);
        return new ResponseEntity<>(service.findById(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> edit(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Validated @RequestBody UpdateEventUserRequest updateEvent
    ) {
        log.info("Patch userId:{} eventId:{}", userId, eventId);
        return new ResponseEntity<>(service.edit(userId, eventId, updateEvent), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Object> findRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("Get requests userId:{} eventId:{}", userId, eventId);
        return new ResponseEntity<>(service.findRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<Object> editRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest request
    ) {
        log.info("Patch requests userId:{} eventId:{}", userId, eventId);
        return new ResponseEntity<>(service.editRequest(userId, eventId, request), HttpStatus.OK);
    }
}
