package ru.practicum.ewmservice.admin_service.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.comments.service.CommentAdminService;

@Controller
@RequestMapping("/admin/comments/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentAdminController {
    private final CommentAdminService service;

    @DeleteMapping("{comId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long comId
    ) {
        log.info("Delete comment");
        service.delete(comId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
