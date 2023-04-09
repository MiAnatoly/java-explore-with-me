package ru.practicum.ewmservice.private_service.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.comments.NewCommentDto;
import ru.practicum.ewmservice.dto.comments.UpdateUserCommentDto;
import ru.practicum.ewmservice.private_service.comments.service.CommentPrivateService;

@Controller
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentsPrivateController {
    private final CommentPrivateService service;

    @PostMapping
    public ResponseEntity<Object> add(
            @PathVariable Long userId,
            @Validated @RequestBody NewCommentDto comment
    ) {
        log.info("Post comment");
        return new ResponseEntity<>(service.add(userId, comment), HttpStatus.CREATED);
    }

    @PatchMapping("/{comId}")
    public ResponseEntity<Object> edit(
            @PathVariable Long userId,
            @PathVariable Long comId,
            @Validated @RequestBody UpdateUserCommentDto commentDto
            ) {
        log.info("Patch comment userId:{}", userId);
        return new ResponseEntity<>(service.edit(userId, comId, commentDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findByUser(
            @PathVariable Long userId
    ) {
        log.info("Get comments userId{}", userId);
        return new ResponseEntity<>(service.findByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/{comId}")
    public ResponseEntity<Object> findById(
            @PathVariable Long userId,
            @PathVariable Long comId
    ) {
        log.info("Get userId:{} comId:{}", userId, comId);
        return new ResponseEntity<>(service.findById(userId, comId), HttpStatus.OK);
    }

    @DeleteMapping("{comId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long userId,
            @PathVariable Long comId
    ) {
        log.info("Delete userId:{} comId:{}", userId, comId);
        service.delete(userId, comId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

