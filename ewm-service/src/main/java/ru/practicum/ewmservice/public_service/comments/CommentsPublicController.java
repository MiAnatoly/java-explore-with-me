package ru.practicum.ewmservice.public_service.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.public_service.comments.service.CommentPublicService;
import ru.practicum.ewmservice.valide.ValidPage;

import javax.validation.constraints.Min;

@Controller
@RequestMapping("/comments/events/{eventId}")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentsPublicController {
    private final CommentPublicService service;

    @GetMapping
    public ResponseEntity<Object> findByEvent(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) Integer size
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get comments eventId{}", eventId);
        return new ResponseEntity<>(service.findByEvent(eventId, page, size), HttpStatus.OK);
    }

}
