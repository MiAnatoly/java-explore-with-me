package ru.practicum.ewmservice.public_service.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewmservice.public_service.events.service.EventsPublicService;
import ru.practicum.ewmservice.valide.ValidPage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventsPublicController {
    private final EventsPublicService service;

    @GetMapping
    public ResponseEntity<Object> search(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get search");
        return new ResponseEntity<>(
                service.search(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, page, size, request),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Get event id:{}", id);
        return new ResponseEntity<>(service.findById(id, request), HttpStatus.OK);
    }
}
