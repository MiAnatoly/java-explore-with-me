package ru.practicum.ewmservice.admin_service.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.events.service.EventsAdminService;
import ru.practicum.ewmservice.dto.events.UpdateEventAdminRequest;
import ru.practicum.ewmservice.status.State;
import ru.practicum.ewmservice.valide.ValidPage;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventsAdminController {
    private final EventsAdminService service;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size
            ) {
        int page = ValidPage.page(from, size);
        List<State> statesList = null;
        if (states != null) {
            statesList = states.stream().map(State::valueOf).collect(Collectors.toList());
        }
        return new ResponseEntity<>(
                service.findAll(users, statesList, categories, rangeStart, rangeEnd, page, size),
                HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> edit(
            @PathVariable Long eventId,
            @Validated @RequestBody UpdateEventAdminRequest updateEvent
    ) {
        return new ResponseEntity<>(service.edit(eventId, updateEvent), HttpStatus.OK);
    }
}
